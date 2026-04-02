-- Check if company exists in hs_hr_geninfo table
SELECT COUNT(*) as company_count
FROM `hs_hr_geninfo`
WHERE `geninfo_keys` LIKE '%COMPANY%';
