DROP TABLE BankTransaction
DROP TABLE BankUser

CREATE TABLE BankUser(
    id int IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(30),
    user_password VARCHAR(30),
    currentBalance DECIMAL DEFAULT 0.0
)

CREATE TABLE BankTransaction(
    id int IDENTITY(1,1) PRIMARY KEY,
    user_id int FOREIGN KEY REFERENCES BankUser(id),
    transactionType VARCHAR(20) CHECK (transactionType in('Deposit', 'Withdraw', 'TransferIn','TransferOut')),
    transactionAmount DECIMAL,
    transactionDate DATE
)
--stored procedure for deposit
go;
CREATE PROC deposit
@id int,
@transactionAmount DECIMAL
AS
INSERT INTO BankTransaction (user_id, transactionType, transactionAmount, transactionDate) VALUES
(@id, 'Deposit', @transactionAmount, GETDATE())

--stored procedure for withdraw
go;
CREATE PROC withdraw
@id int,
@transactionAmount DECIMAL
AS
INSERT INTO BankTransaction (user_id, transactionType, transactionAmount, transactionDate) VALUES
(@id, 'Withdraw', @transactionAmount, GETDATE())


--trigger after inserting a deposit or withdrawal
go;
CREATE TRIGGER after_deposit_or_withdraw ON BankTransaction
AFTER INSERT
AS
BEGIN
DECLARE @userID int;
DECLARE @transactionType VARCHAR(30);
DECLARE @transactionAmount DECIMAL;

SELECT @userID = user_id, @transactionType = transactionType, @transactionAmount = transactionAmount
FROM inserted

IF (@transactionType = 'Deposit')
BEGIN
    UPDATE BankUser
    SET currentBalance = currentBalance + @transactionAmount
    WHERE id = @userID
END

ELSE IF (@transactionType = 'Withdraw')
BEGIN
IF EXISTS (SELECT 1 FROM BankUser WHERE id = @userID and currentBalance >= @transactionAmount)
BEGIN
    UPDATE BankUser
    SET currentBalance = currentBalance - @transactionAmount
    WHERE id = @userID
END
ELSE
    BEGIN
    PRINT 'Withdrawal is invalid'
    END
END
END


--trigger after transfer
drop TRIGGER after_transfer_out
go;
CREATE TRIGGER after_transfer_out ON BankTransaction
AFTER INSERT
AS
BEGIN
DECLARE @userID int;
DECLARE @transactionType VARCHAR(30);
DECLARE @transactionAmount DECIMAL;

SELECT @userID= user_id, @transactionType = transactionType, @transactionAmount = transactionAmount
FROM inserted

IF (@transactionType = 'TransferOut')
BEGIN
    IF EXISTS (SELECT 1 FROM BankUser WHERE id = @userID and currentBalance >= @transactionAmount)
BEGIN
    UPDATE BankUser
    SET currentBalance = currentBalance - @transactionAmount
    WHERE id = @userID
END
ELSE
    BEGIN
    PRINT 'Transfer is invalid'
    END
END
END

--trigger for after transfer IN
drop TRIGGER after_transfer_in
go;
CREATE TRIGGER after_transfer_in ON BankTransaction
AFTER INSERT
AS
BEGIN
DECLARE @userID int;
DECLARE @transactionType VARCHAR(30);
DECLARE @transactionAmount DECIMAL;

SELECT @userID= user_id, @transactionType = transactionType, @transactionAmount = transactionAmount
FROM inserted

IF (@transactionType = 'TransferIn')
BEGIN
    UPDATE BankUser
    SET currentBalance = currentBalance + @transactionAmount
    WHERE id = @userID
END
END

--stored procedure for past transactions
go;
CREATE PROC pastTransactions
@id int
AS
SELECT user_id, transactionType, transactionAmount, transactionDate FROM BankTransaction
INNER JOIN BankUser ON BankUser.id = user_id
WHERE user_id = @id

--stored procedure for transfer
drop PROC transferAction
go;
CREATE PROC transferAction
@fromId int,
@toId int,
@transferAmount DECIMAL
AS
INSERT INTO BankTransaction (user_id, transactionType, transactionAmount, transactionDate) VALUES
(@fromId, 'TransferOut', @transferAmount, GETDATE())
INSERT INTO BankTransaction (user_id, transactionType, transactionAmount, transactionDate) VALUES
(@toId, 'TransferIn', @transferAmount, GETDATE())




--test data
INSERT INTO BankUser(username, user_password) VALUES
('test', '12345'),
--('Løve', '123', 20.00),
--('Løve', '123', 20.00)

EXEC deposit 1, 20
EXEC withdraw 1, 30
EXEC pastTransactions 1

EXEC transferAction 1, 3, 20
SELECT * FROM BankUser 

SELECT * FROM BankTransaction
INNER JOIN BankUser ON user_id = BankUser.id
WHERE user_id = 1

SELECT id FROM BankUser WHERE username = 'Malthebb'
