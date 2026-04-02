-- Get company address details from hs_hr_geninfo table
SELECT `address1`, `address2`, `city`, `state`, `zipcode`, `country`, `phone`, `fax` 
FROM `hs_hr_geninfo` 
WHERE `name` = ?;
