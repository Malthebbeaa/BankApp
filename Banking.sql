use bankdb
DROP TABLE IF EXISTS Konto 
DROP TABLE IF EXISTS BankTransaction
DROP TABLE IF EXISTS BankUser

CREATE TABLE BankUser(
    user_id int IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(30),
    user_password NVARCHAR(45),
    salt VARBINARY(45)
)

CREATE TABLE Konto (
    user_id int FOREIGN KEY REFERENCES BankUser(user_id),
    kontoNr char(7),
    regNr char(4),
    saldo DECIMAL(10,2),
    kontoType VARCHAR(20),
    PRIMARY KEY (kontoNr, regNr)
)

CREATE TABLE BankTransaction(
    transactionId int IDENTITY(1,1) PRIMARY KEY,
    user_id int FOREIGN KEY REFERENCES BankUser(user_id),
    kontoNr char(7),
    regNr CHAR(4),
    transactionType VARCHAR(20) CHECK (transactionType in('Deposit', 'Withdraw', 'TransferIn','TransferOut')),
    transactionAmount DECIMAL,
    transactionDate DATE
)

--stored procedure for making user
go;
CREATE OR ALTER PROC createUser
@username VARCHAR(30),
@hashedpassword NVARCHAR(45),
@salt VARBINARY(45)
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY
        IF EXISTS (SELECT 1 FROM BankUser where username = @username)
            THROW 50006, 'Brugernavnet er optaget', 1;

        INSERT INTO BankUser (username, user_password, salt) VALUES
        (@username, @hashedpassword, @salt);

        COMMIT
    END TRY
    BEGIN CATCH
        ROLLBACK
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 5003, @ErrorMessage, 1;
    END CATCH
END;

--stored procedure for making account
go;
CREATE OR ALTER PROC createAccount
@userid int,
@initialSaldo DECIMAL(10,2),
@type VARCHAR(20)
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY
        DECLARE @kontoNr char(7)
        DECLARE @regNr char(4)

        SET @kontoNr = RIGHT('0000000' + CAST(ABS(CHECKSUM(NEWID())) % 10000000 AS VARCHAR), 7);
        SET @regNr = RIGHT('0000' + CAST(ABS(CHECKSUM(NEWID())) % 10000 AS VARCHAR), 4);

        INSERT INTO Konto (user_id, kontoNr, regNr, saldo, kontoType) VALUES
        (@userID, @kontoNr, @regNr, @initialSaldo, @type);

        COMMIT
    END TRY
    BEGIN CATCH
        ROLLBACK

        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 50001, @ErrorMessage, 1;
    END CATCH
END;

--stored procedure for deposit
go;
CREATE or ALTER PROC deposit
@id int,
@transactionAmount DECIMAL,
@kontoNr CHAR(7),
@regNr CHAR(4)
AS
BEGIN
    BEGIN TRANSACTION  
    BEGIN TRY 
        IF @transactionAmount <= 0
            THROW 50001, 'Beløbet skal være større end 0',1;
        
        IF NOT EXISTS (SELECT 1 FROM Konto WHERE kontoNr = @kontoNr AND regNr = @regNr AND user_id = @id)
            THROW 50002, 'Kontoen blev ikke fundet eller tilhører ikke brugeren', 1;

        INSERT INTO BankTransaction (user_id, kontoNr, regNr,transactionType, transactionAmount, transactionDate) VALUES
        (@id, @kontoNr, @regNr, 'Deposit', @transactionAmount, GETDATE())

        COMMIT
    END TRY
    BEGIN CATCH
        ROLLBACK

        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 50003, @ErrorMessage, 1;
    END CATCH

END;

--stored procedure for withdraw
go;
CREATE or ALTER PROC withdraw
@id int,
@transactionAmount DECIMAL,
@kontoNr CHAR(7),
@regNr char(4)
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY
        IF @transactionAmount <= 0
            THROW 50001, 'Beløbet skal være over 0',1
        
        IF NOT EXISTS (SELECT 1 FROM Konto WHERE kontoNr = @kontoNr AND regNr = @regNr AND user_id = @id)
            THROW 50002, 'Kontoen blev ikke fundet eller tilhører ikke brugeren', 1

        IF @transactionAmount > (SELECT saldo FROM Konto WHERE kontoNr = @kontoNr AND regNr = @regNr AND user_id = @id)
            THROW 50004, 'Beløbet på hævningen overskrider din nuværende saldo', 1

        INSERT INTO BankTransaction (user_id, kontoNr, regNr, transactionType ,transactionAmount, transactionDate) VALUES
        (@id, @kontoNr, @regNr,'Withdraw', @transactionAmount, GETDATE())

        COMMIT
    END TRY
    BEGIN CATCH
        ROLLBACK

        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 50005, @ErrorMessage, 1;
    END CATCH
END

--trigger after inserting a deposit or withdrawal
go;
CREATE OR ALTER TRIGGER after_deposit_or_withdraw ON BankTransaction
AFTER INSERT
AS
BEGIN
DECLARE @userID int;
DECLARE @transactionType VARCHAR(30);
DECLARE @transactionAmount DECIMAL;
DECLARE @kontoNr CHAR(7);
DECLARE @regNr CHAR(4);

SELECT @userID = user_id, @kontoNr = kontoNr, @regNr = regNr, @transactionType = transactionType, @transactionAmount = transactionAmount
FROM inserted

BEGIN TRANSACTION
IF (@transactionType = 'Deposit')
BEGIN
    UPDATE Konto
    SET saldo = saldo + @transactionAmount
    WHERE kontoNr = @kontoNr AND regNr = @regNr
END

ELSE IF (@transactionType = 'Withdraw')
BEGIN
IF EXISTS (SELECT 1 
            FROM Konto
            WHERE kontoNr = @kontoNr AND regNr = @regNr AND saldo >= @transactionAmount)
BEGIN
    UPDATE Konto
    SET saldo = saldo - @transactionAmount
    WHERE kontoNr = @kontoNr AND regNr = @regNr
END
ELSE
    BEGIN
    ROLLBACK
    PRINT 'Withdrawal is invalid'
    END
END
COMMIT
END


--trigger after transfer
drop TRIGGER after_transfer_out
go;
CREATE OR ALTER TRIGGER after_transfer_out ON BankTransaction
AFTER INSERT
AS
BEGIN
DECLARE @userID int;
DECLARE @transactionType VARCHAR(30);
DECLARE @transactionAmount DECIMAL;
DECLARE @kontoNr CHAR(7);
DECLARE @regNr CHAR(4);

SELECT @userID= user_id, @kontoNr = kontoNr, @regNr = regNr, @transactionType = transactionType, @transactionAmount = transactionAmount
FROM inserted
BEGIN TRANSACTION
IF (@transactionType = 'TransferOut')
BEGIN
    IF EXISTS (SELECT 1 
                FROM BankUser 
                JOIN Konto ON BankUser.user_id = Konto.user_id
                WHERE kontoNr = @kontoNr AND regNr = @regNr and saldo >= @transactionAmount)
BEGIN
    UPDATE Konto
    SET saldo = saldo - @transactionAmount
    WHERE kontoNr = @kontoNr AND regNr = @regNr
END
ELSE
    BEGIN
    ROLLBACK
    PRINT 'Transfer is invalid'
    END
END
COMMIT
END

--trigger for after transfer IN
drop TRIGGER after_transfer_in
go;
CREATE OR ALTER TRIGGER after_transfer_in ON BankTransaction
AFTER INSERT
AS
BEGIN
DECLARE @userID int;
DECLARE @transactionType VARCHAR(30);
DECLARE @transactionAmount DECIMAL;
DECLARE @kontoNr CHAR(7);
DECLARE @regNr CHAR(4);

SELECT @userID= user_id, @kontoNr = kontoNr, @regNr = regNr, @transactionType = transactionType, @transactionAmount = transactionAmount
FROM inserted
IF (@transactionType = 'TransferIn')
BEGIN
    UPDATE Konto
    SET saldo = saldo + @transactionAmount
    WHERE kontoNr = @kontoNr AND regNr = @regNr
END
END

--stored procedure for past transactions
go;
CREATE OR ALTER PROC pastTransactions
@id int
AS
SELECT BankUser.user_id, transactionType, transactionAmount, transactionDate FROM BankTransaction
INNER JOIN BankUser ON BankUser.user_id = BankTransaction.user_id
WHERE BankUser.user_id = @id

--stored procedure for transfer
go;
CREATE OR ALTER PROC transferAction
@fromId int,
@toId int,
@transferAmount DECIMAL,
@fromKontoNr CHAR(7),
@fromRegNr CHAR(4),
@toKontoNr char(7),
@toRegNr CHAR(4)
AS
BEGIN
    BEGIN TRANSACTION
    BEGIN TRY

        INSERT INTO BankTransaction (user_id, kontoNr, regNr, transactionType, transactionAmount, transactionDate) VALUES
        (@fromId, @fromKontoNr, @fromRegNr,'TransferOut', @transferAmount, GETDATE())
        INSERT INTO BankTransaction (user_id, kontoNr, regNr, transactionType, transactionAmount, transactionDate) VALUES
        (@toId, @toKontoNr, @toRegNr, 'TransferIn', @transferAmount, GETDATE())

        COMMIT
    END TRY
    BEGIN CATCH
        ROLLBACK
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        THROW 50005, @ErrorMessage, 1;
    END CATCH
END;



EXEC createAccount @userid = 1, @initialSaldo = 25000.00, @type = 'BoligOpsparing';

EXEC deposit 1, 2500.00, '9349759', '4830' 

exec pastTransactions 1

SELECT top 10 * from Konto where user_id = 1
SELECT top 10 * from BankUser
SELECT top 10 * from BankTransaction

