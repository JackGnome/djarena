INSERT INTO product (vendor_code, name, price, preview, brand_id)
VALUES ('179032',
        'AKG PCC130 SW',
        42999,
        'http://localhost:9000/product-preview/179032.png',
        (select id from brand where name = 'AKG')),

       ('179033',
        'AKG Crown MB3',
        13998,
        'http://localhost:9000/product-preview/179033.png',
        (select id from brand where name = 'AKG')),

       ('179034',
        'AMX CBL31 WLS',
        1000,
        'http://localhost:9000/product-preview/179034.png',
        (select id from brand where name = 'AMX'));