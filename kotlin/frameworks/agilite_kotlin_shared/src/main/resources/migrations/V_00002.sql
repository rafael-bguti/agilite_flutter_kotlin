--- Pré --- 
CREATE TABLE IF NOT exists  db_migration(name VARCHAR(20) NOT NULL PRIMARY KEY, time_run timestamp not null);

--- SQL ---
CREATE TABLE IF NOT EXISTS cas29 (cas29id SERIAL NOT NULL, CONSTRAINT cas29pk PRIMARY KEY(cas29id));
CREATE TABLE IF NOT EXISTS cas30 (cas30id SERIAL NOT NULL, CONSTRAINT cas30pk PRIMARY KEY(cas30id));
CREATE TABLE IF NOT EXISTS cas65 (cas65id SERIAL NOT NULL, CONSTRAINT cas65pk PRIMARY KEY(cas65id));
CREATE TABLE IF NOT EXISTS cas90 (cas90id SERIAL NOT NULL, CONSTRAINT cas90pk PRIMARY KEY(cas90id));
CREATE TABLE IF NOT EXISTS cgs15 (cgs15id SERIAL NOT NULL, CONSTRAINT cgs15pk PRIMARY KEY(cgs15id));
CREATE TABLE IF NOT EXISTS cgs18 (cgs18id SERIAL NOT NULL, CONSTRAINT cgs18pk PRIMARY KEY(cgs18id));
CREATE TABLE IF NOT EXISTS cgs38 (cgs38id SERIAL NOT NULL, CONSTRAINT cgs38pk PRIMARY KEY(cgs38id));
CREATE TABLE IF NOT EXISTS cgs40 (cgs40id SERIAL NOT NULL, CONSTRAINT cgs40pk PRIMARY KEY(cgs40id));
CREATE TABLE IF NOT EXISTS cgs45 (cgs45id SERIAL NOT NULL, CONSTRAINT cgs45pk PRIMARY KEY(cgs45id));
CREATE TABLE IF NOT EXISTS cgs50 (cgs50id SERIAL NOT NULL, CONSTRAINT cgs50pk PRIMARY KEY(cgs50id));
CREATE TABLE IF NOT EXISTS cgs80 (cgs80id SERIAL NOT NULL, CONSTRAINT cgs80pk PRIMARY KEY(cgs80id));
CREATE TABLE IF NOT EXISTS gdf10 (gdf10id SERIAL NOT NULL, CONSTRAINT gdf10pk PRIMARY KEY(gdf10id));
CREATE TABLE IF NOT EXISTS scf02 (scf02id SERIAL NOT NULL, CONSTRAINT scf02pk PRIMARY KEY(scf02id));
CREATE TABLE IF NOT EXISTS scf021 (scf021id SERIAL NOT NULL, CONSTRAINT scf021pk PRIMARY KEY(scf021id));
CREATE TABLE IF NOT EXISTS scf11 (scf11id SERIAL NOT NULL, CONSTRAINT scf11pk PRIMARY KEY(scf11id));
CREATE TABLE IF NOT EXISTS srf01 (srf01id SERIAL NOT NULL, CONSTRAINT srf01pk PRIMARY KEY(srf01id));
CREATE TABLE IF NOT EXISTS srf011 (srf011id SERIAL NOT NULL, CONSTRAINT srf011pk PRIMARY KEY(srf011id));
CREATE TABLE IF NOT EXISTS srf012 (srf012id SERIAL NOT NULL, CONSTRAINT srf012pk PRIMARY KEY(srf012id));

ALTER TABLE cas29 ADD COLUMN IF NOT EXISTS cas29nome VARCHAR(50) ;
UPDATE cas29 SET cas29nome = ' ' WHERE cas29nome IS NULL;
ALTER TABLE cas29 ALTER COLUMN cas29nome SET NOT NULL;

DROP INDEX IF EXISTS cas29_uk;
CREATE UNIQUE INDEX cas29_uk ON cas29 (cas29nome);
DROP INDEX IF EXISTS cas29_mk;
CREATE INDEX cas29_mk ON cas29 (cas29nome);

ALTER TABLE cas30 ADD COLUMN IF NOT EXISTS cas30autenticacao BIGINT ;
UPDATE cas30 SET cas30autenticacao = 0 WHERE cas30autenticacao IS NULL;
ALTER TABLE cas30 ALTER COLUMN cas30autenticacao SET NOT NULL;

ALTER TABLE cas30 ADD COLUMN IF NOT EXISTS cas30nome VARCHAR(30) ;
UPDATE cas30 SET cas30nome = ' ' WHERE cas30nome IS NULL;
ALTER TABLE cas30 ALTER COLUMN cas30nome SET NOT NULL;

ALTER TABLE cas30 ADD COLUMN IF NOT EXISTS cas30empativa BIGINT ;
ALTER TABLE cas30 ALTER COLUMN cas30empativa SET NOT NULL;

ALTER TABLE cas30 ADD COLUMN IF NOT EXISTS cas30credencial BIGINT ;
ALTER TABLE cas30 ALTER COLUMN cas30credencial SET NOT NULL;

ALTER TABLE cas30 ADD COLUMN IF NOT EXISTS cas30interno BOOLEAN ;

DROP INDEX IF EXISTS cas30_uk;
CREATE UNIQUE INDEX cas30_uk ON cas30 (cas30autenticacao);
DROP INDEX IF EXISTS cas30_mk;
CREATE INDEX cas30_mk ON cas30 (cas30autenticacao);

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65cnpj VARCHAR(14) ;
UPDATE cas65 SET cas65cnpj = ' ' WHERE cas65cnpj IS NULL;
ALTER TABLE cas65 ALTER COLUMN cas65cnpj SET NOT NULL;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65im VARCHAR(20) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65nome VARCHAR(100) ;
UPDATE cas65 SET cas65nome = ' ' WHERE cas65nome IS NULL;
ALTER TABLE cas65 ALTER COLUMN cas65nome SET NOT NULL;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65cnae VARCHAR(10) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65cep VARCHAR(8) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65endereco VARCHAR(200) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65bairro VARCHAR(50) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65numero VARCHAR(50) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65complemento VARCHAR(50) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65uf VARCHAR(2) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65municipio VARCHAR(50) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailhost VARCHAR(100) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailport INTEGER ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailuser VARCHAR(100) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailpass VARCHAR(100) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailfrom VARCHAR(100) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailfromname VARCHAR(100) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailreplyto VARCHAR(100) ;

ALTER TABLE cas65 ADD COLUMN IF NOT EXISTS cas65mailtls BOOLEAN ;

DROP INDEX IF EXISTS cas65_uk;
CREATE UNIQUE INDEX cas65_uk ON cas65 (cas65cnpj);
DROP INDEX IF EXISTS cas65_mk;
CREATE INDEX cas65_mk ON cas65 (cas65cnpj);

ALTER TABLE cas90 ADD COLUMN IF NOT EXISTS cas90codigo VARCHAR(100) ;
UPDATE cas90 SET cas90codigo = ' ' WHERE cas90codigo IS NULL;
ALTER TABLE cas90 ALTER COLUMN cas90codigo SET NOT NULL;

ALTER TABLE cas90 ADD COLUMN IF NOT EXISTS cas90descr TEXT ;

ALTER TABLE cas90 ADD COLUMN IF NOT EXISTS cas90grupo SMALLINT ;
UPDATE cas90 SET cas90grupo = 0 WHERE cas90grupo IS NULL;
ALTER TABLE cas90 ALTER COLUMN cas90grupo SET NOT NULL;

DROP INDEX IF EXISTS cas90_uk;
CREATE UNIQUE INDEX cas90_uk ON cas90 (cas90codigo, cas90grupo);
DROP INDEX IF EXISTS cas90_mk;
CREATE INDEX cas90_mk ON cas90 (cas90codigo, cas90grupo);

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15empresa BIGINT ;
UPDATE cgs15 SET cgs15empresa = 0 WHERE cgs15empresa IS NULL;
ALTER TABLE cgs15 ALTER COLUMN cgs15empresa SET NOT NULL;

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15nome VARCHAR(30) ;
UPDATE cgs15 SET cgs15nome = ' ' WHERE cgs15nome IS NULL;
ALTER TABLE cgs15 ALTER COLUMN cgs15nome SET NOT NULL;

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15template TEXT ;
UPDATE cgs15 SET cgs15template = ' ' WHERE cgs15template IS NULL;
ALTER TABLE cgs15 ALTER COLUMN cgs15template SET NOT NULL;

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15titulo VARCHAR(100) ;

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15fromname VARCHAR(100) ;

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15replayto VARCHAR(100) ;

ALTER TABLE cgs15 ADD COLUMN IF NOT EXISTS cgs15replaytoname VARCHAR(100) ;

DROP INDEX IF EXISTS cgs15_uk;
CREATE UNIQUE INDEX cgs15_uk ON cgs15 (cgs15empresa, cgs15nome);
DROP INDEX IF EXISTS cgs15_mk;
CREATE INDEX cgs15_mk ON cgs15 (cgs15empresa, cgs15nome);

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18empresa BIGINT ;
UPDATE cgs18 SET cgs18empresa = 0 WHERE cgs18empresa IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18empresa SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18codigo VARCHAR(10) ;
UPDATE cgs18 SET cgs18codigo = ' ' WHERE cgs18codigo IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18codigo SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18nome VARCHAR(100) ;
UPDATE cgs18 SET cgs18nome = ' ' WHERE cgs18nome IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18nome SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18tipo SMALLINT ;
UPDATE cgs18 SET cgs18tipo = 0 WHERE cgs18tipo IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18tipo SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18es SMALLINT ;
UPDATE cgs18 SET cgs18es = 0 WHERE cgs18es IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18es SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18scf SMALLINT ;
UPDATE cgs18 SET cgs18scf = 0 WHERE cgs18scf IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18scf SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18serie SMALLINT ;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18emitirdoc BOOLEAN ;
UPDATE cgs18 SET cgs18emitirdoc = false WHERE cgs18emitirdoc IS NULL;
ALTER TABLE cgs18 ALTER COLUMN cgs18emitirdoc SET NOT NULL;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18alqiss NUMERIC(4,2) ;

ALTER TABLE cgs18 ADD COLUMN IF NOT EXISTS cgs18modeloemail BIGINT ;

DROP INDEX IF EXISTS cgs18_uk;
CREATE UNIQUE INDEX cgs18_uk ON cgs18 (cgs18empresa, cgs18codigo);
DROP INDEX IF EXISTS cgs18_mk;
CREATE INDEX cgs18_mk ON cgs18 (cgs18empresa, cgs18codigo);

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38empresa BIGINT ;
UPDATE cgs38 SET cgs38empresa = 0 WHERE cgs38empresa IS NULL;
ALTER TABLE cgs38 ALTER COLUMN cgs38empresa SET NOT NULL;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38nome VARCHAR(30) ;
UPDATE cgs38 SET cgs38nome = ' ' WHERE cgs38nome IS NULL;
ALTER TABLE cgs38 ALTER COLUMN cgs38nome SET NOT NULL;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38tipo SMALLINT ;
UPDATE cgs38 SET cgs38tipo = 0 WHERE cgs38tipo IS NULL;
ALTER TABLE cgs38 ALTER COLUMN cgs38tipo SET NOT NULL;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38gerar SMALLINT ;
UPDATE cgs38 SET cgs38gerar = 0 WHERE cgs38gerar IS NULL;
ALTER TABLE cgs38 ALTER COLUMN cgs38gerar SET NOT NULL;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38forma SMALLINT ;
UPDATE cgs38 SET cgs38forma = 0 WHERE cgs38forma IS NULL;
ALTER TABLE cgs38 ALTER COLUMN cgs38forma SET NOT NULL;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38conta BIGINT ;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38apiclientid VARCHAR(100) ;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38apiclientsecret VARCHAR(100) ;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38apicert TEXT ;

ALTER TABLE cgs38 ADD COLUMN IF NOT EXISTS cgs38apikey TEXT ;

DROP INDEX IF EXISTS cgs38_uk;
CREATE UNIQUE INDEX cgs38_uk ON cgs38 (cgs38empresa, cgs38nome);
DROP INDEX IF EXISTS cgs38_mk;
CREATE INDEX cgs38_mk ON cgs38 (cgs38empresa, cgs38nome);

ALTER TABLE cgs40 ADD COLUMN IF NOT EXISTS cgs40empresa BIGINT ;
UPDATE cgs40 SET cgs40empresa = 0 WHERE cgs40empresa IS NULL;
ALTER TABLE cgs40 ALTER COLUMN cgs40empresa SET NOT NULL;

ALTER TABLE cgs40 ADD COLUMN IF NOT EXISTS cgs40descr VARCHAR(30) ;
UPDATE cgs40 SET cgs40descr = ' ' WHERE cgs40descr IS NULL;
ALTER TABLE cgs40 ALTER COLUMN cgs40descr SET NOT NULL;

ALTER TABLE cgs40 ADD COLUMN IF NOT EXISTS cgs40tipo SMALLINT ;
UPDATE cgs40 SET cgs40tipo = 0 WHERE cgs40tipo IS NULL;
ALTER TABLE cgs40 ALTER COLUMN cgs40tipo SET NOT NULL;

ALTER TABLE cgs40 ADD COLUMN IF NOT EXISTS cgs40sup BIGINT ;

ALTER TABLE cgs40 ADD COLUMN IF NOT EXISTS cgs40grupo BOOLEAN ;
UPDATE cgs40 SET cgs40grupo = false WHERE cgs40grupo IS NULL;
ALTER TABLE cgs40 ALTER COLUMN cgs40grupo SET NOT NULL;

DROP INDEX IF EXISTS cgs40_uk;
CREATE UNIQUE INDEX cgs40_uk ON cgs40 (cgs40empresa, cgs40descr);
DROP INDEX IF EXISTS cgs40_mk;
CREATE INDEX cgs40_mk ON cgs40 (cgs40empresa, cgs40descr);

ALTER TABLE cgs45 ADD COLUMN IF NOT EXISTS cgs45empresa BIGINT ;
UPDATE cgs45 SET cgs45empresa = 0 WHERE cgs45empresa IS NULL;
ALTER TABLE cgs45 ALTER COLUMN cgs45empresa SET NOT NULL;

ALTER TABLE cgs45 ADD COLUMN IF NOT EXISTS cgs45nome VARCHAR(30) ;
UPDATE cgs45 SET cgs45nome = ' ' WHERE cgs45nome IS NULL;
ALTER TABLE cgs45 ALTER COLUMN cgs45nome SET NOT NULL;

ALTER TABLE cgs45 ADD COLUMN IF NOT EXISTS cgs45dtultprocretorno DATE ;

DROP INDEX IF EXISTS cgs45_uk;
CREATE UNIQUE INDEX cgs45_uk ON cgs45 (cgs45empresa, cgs45nome);
DROP INDEX IF EXISTS cgs45_mk;
CREATE INDEX cgs45_mk ON cgs45 (cgs45empresa, cgs45nome);

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50empresa BIGINT ;
UPDATE cgs50 SET cgs50empresa = 0 WHERE cgs50empresa IS NULL;
ALTER TABLE cgs50 ALTER COLUMN cgs50empresa SET NOT NULL;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50mps SMALLINT ;
UPDATE cgs50 SET cgs50mps = 0 WHERE cgs50mps IS NULL;
ALTER TABLE cgs50 ALTER COLUMN cgs50mps SET NOT NULL;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50descr TEXT ;
UPDATE cgs50 SET cgs50descr = ' ' WHERE cgs50descr IS NULL;
ALTER TABLE cgs50 ALTER COLUMN cgs50descr SET NOT NULL;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50codigo VARCHAR(15) ;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50preco NUMERIC(16,2) ;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50unidade VARCHAR(30) ;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50descrcomp TEXT ;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50obs TEXT ;

ALTER TABLE cgs50 ADD COLUMN IF NOT EXISTS cgs50tiposervico VARCHAR(10) ;

DROP INDEX IF EXISTS cgs50_uk;
CREATE UNIQUE INDEX cgs50_uk ON cgs50 (cgs50empresa, cgs50codigo);
DROP INDEX IF EXISTS cgs50_mk;
CREATE INDEX cgs50_mk ON cgs50 (cgs50empresa, cgs50codigo);

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80empresa BIGINT ;
UPDATE cgs80 SET cgs80empresa = 0 WHERE cgs80empresa IS NULL;
ALTER TABLE cgs80 ALTER COLUMN cgs80empresa SET NOT NULL;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80nome VARCHAR(250) ;
UPDATE cgs80 SET cgs80nome = ' ' WHERE cgs80nome IS NULL;
ALTER TABLE cgs80 ALTER COLUMN cgs80nome SET NOT NULL;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80fantasia VARCHAR(100) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80codigo VARCHAR(15) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80tipo SMALLINT ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80ni VARCHAR(20) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80contribuinte SMALLINT ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80ie VARCHAR(30) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80im VARCHAR(30) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80cep VARCHAR(8) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80endereco VARCHAR(200) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80bairro VARCHAR(50) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80numero VARCHAR(50) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80complemento VARCHAR(50) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80uf VARCHAR(2) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80municipio VARCHAR(50) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80telefone VARCHAR(20) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80celular VARCHAR(20) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80email VARCHAR(200) ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80obs TEXT ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80cliente BOOLEAN ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80fornecedor BOOLEAN ;

ALTER TABLE cgs80 ADD COLUMN IF NOT EXISTS cgs80transportadora BOOLEAN ;

DROP INDEX IF EXISTS cgs80_uk;
CREATE UNIQUE INDEX cgs80_uk ON cgs80 (cgs80empresa, cgs80codigo);
DROP INDEX IF EXISTS cgs80_mk;
CREATE INDEX cgs80_mk ON cgs80 (cgs80empresa, cgs80codigo);

DROP INDEX IF EXISTS cgs80_uk1;
CREATE UNIQUE INDEX cgs80_uk1 ON cgs80 (cgs80empresa, cgs80ni);
DROP INDEX IF EXISTS cgs80_mk1;
CREATE INDEX cgs80_mk1 ON cgs80 (cgs80empresa, cgs80ni);

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10empresa BIGINT ;
UPDATE gdf10 SET gdf10empresa = 0 WHERE gdf10empresa IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10empresa SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10dtemiss DATE ;
UPDATE gdf10 SET gdf10dtemiss = '2000-01-01' WHERE gdf10dtemiss IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10dtemiss SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10hremiss TIME ;
UPDATE gdf10 SET gdf10hremiss = '00:00:00' WHERE gdf10hremiss IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10hremiss SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10sistema SMALLINT ;
UPDATE gdf10 SET gdf10sistema = 0 WHERE gdf10sistema IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10sistema SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10tipodoc SMALLINT ;
UPDATE gdf10 SET gdf10tipodoc = 0 WHERE gdf10tipodoc IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10tipodoc SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10documento TEXT ;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10statusproc SMALLINT ;
UPDATE gdf10 SET gdf10statusproc = 0 WHERE gdf10statusproc IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10statusproc SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10protocolo VARCHAR(40) ;
UPDATE gdf10 SET gdf10protocolo = ' ' WHERE gdf10protocolo IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10protocolo SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10cstat VARCHAR(20) ;
UPDATE gdf10 SET gdf10cstat = ' ' WHERE gdf10cstat IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10cstat SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10xmotivo TEXT ;
UPDATE gdf10 SET gdf10xmotivo = ' ' WHERE gdf10xmotivo IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10xmotivo SET NOT NULL;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10uidtrack VARCHAR(100) ;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10linkpdf TEXT ;

ALTER TABLE gdf10 ADD COLUMN IF NOT EXISTS gdf10regorigem JSONB ;
UPDATE gdf10 SET gdf10regorigem = '{}' WHERE gdf10regorigem IS NULL;
ALTER TABLE gdf10 ALTER COLUMN gdf10regorigem SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02empresa BIGINT ;
UPDATE scf02 SET scf02empresa = 0 WHERE scf02empresa IS NULL;
ALTER TABLE scf02 ALTER COLUMN scf02empresa SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02tipo SMALLINT ;
UPDATE scf02 SET scf02tipo = 0 WHERE scf02tipo IS NULL;
ALTER TABLE scf02 ALTER COLUMN scf02tipo SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02forma BIGINT ;
ALTER TABLE scf02 ALTER COLUMN scf02forma SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02entidade BIGINT ;
ALTER TABLE scf02 ALTER COLUMN scf02entidade SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02nossonum BIGINT ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02nossonumdv SMALLINT ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02dtemiss DATE ;
UPDATE scf02 SET scf02dtemiss = '2000-01-01' WHERE scf02dtemiss IS NULL;
ALTER TABLE scf02 ALTER COLUMN scf02dtemiss SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02dtvenc DATE ;
UPDATE scf02 SET scf02dtvenc = '2000-01-01' WHERE scf02dtvenc IS NULL;
ALTER TABLE scf02 ALTER COLUMN scf02dtvenc SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02hist TEXT ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02valor NUMERIC(16,2) ;
UPDATE scf02 SET scf02valor = 0 WHERE scf02valor IS NULL;
ALTER TABLE scf02 ALTER COLUMN scf02valor SET NOT NULL;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02multa NUMERIC(16,2) ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02juros NUMERIC(16,2) ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02encargos NUMERIC(16,2) ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02desconto NUMERIC(16,2) ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02regorigem JSONB ;

ALTER TABLE scf02 ADD COLUMN IF NOT EXISTS scf02lancamento BIGINT ;

ALTER TABLE scf021 ADD COLUMN IF NOT EXISTS scf021doc BIGINT ;
ALTER TABLE scf021 ALTER COLUMN scf021doc SET NOT NULL;

ALTER TABLE scf021 ADD COLUMN IF NOT EXISTS scf021remnumero TEXT ;
UPDATE scf021 SET scf021remnumero = ' ' WHERE scf021remnumero IS NULL;
ALTER TABLE scf021 ALTER COLUMN scf021remnumero SET NOT NULL;

ALTER TABLE scf021 ADD COLUMN IF NOT EXISTS scf021conta BIGINT ;
ALTER TABLE scf021 ALTER COLUMN scf021conta SET NOT NULL;

ALTER TABLE scf021 ADD COLUMN IF NOT EXISTS scf021dtbaixa DATE ;

DROP INDEX IF EXISTS scf021_uk;
CREATE UNIQUE INDEX scf021_uk ON scf021 (scf021remnumero);
DROP INDEX IF EXISTS scf021_mk;
CREATE INDEX scf021_mk ON scf021 (scf021remNumero);

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11empresa BIGINT ;
UPDATE scf11 SET scf11empresa = 0 WHERE scf11empresa IS NULL;
ALTER TABLE scf11 ALTER COLUMN scf11empresa SET NOT NULL;

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11tipo SMALLINT ;
UPDATE scf11 SET scf11tipo = 0 WHERE scf11tipo IS NULL;
ALTER TABLE scf11 ALTER COLUMN scf11tipo SET NOT NULL;

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11conta BIGINT ;
ALTER TABLE scf11 ALTER COLUMN scf11conta SET NOT NULL;

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11data DATE ;
UPDATE scf11 SET scf11data = '2000-01-01' WHERE scf11data IS NULL;
ALTER TABLE scf11 ALTER COLUMN scf11data SET NOT NULL;

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11valor NUMERIC(16,2) ;
UPDATE scf11 SET scf11valor = 0 WHERE scf11valor IS NULL;
ALTER TABLE scf11 ALTER COLUMN scf11valor SET NOT NULL;

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11hist TEXT ;
UPDATE scf11 SET scf11hist = ' ' WHERE scf11hist IS NULL;
ALTER TABLE scf11 ALTER COLUMN scf11hist SET NOT NULL;

ALTER TABLE scf11 ADD COLUMN IF NOT EXISTS scf11regorigem JSONB ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01empresa BIGINT ;
UPDATE srf01 SET srf01empresa = 0 WHERE srf01empresa IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01empresa SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01natureza BIGINT ;
ALTER TABLE srf01 ALTER COLUMN srf01natureza SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01tipo SMALLINT ;
UPDATE srf01 SET srf01tipo = 0 WHERE srf01tipo IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01tipo SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01es SMALLINT ;
UPDATE srf01 SET srf01es = 0 WHERE srf01es IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01es SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01ep BOOLEAN ;
UPDATE srf01 SET srf01ep = false WHERE srf01ep IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01ep SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01numero INTEGER ;
UPDATE srf01 SET srf01numero = 0 WHERE srf01numero IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01numero SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01serie SMALLINT ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01dtemiss DATE ;
UPDATE srf01 SET srf01dtemiss = '2000-01-01' WHERE srf01dtemiss IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01dtemiss SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01hremiss TIME ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01entidade BIGINT ;
ALTER TABLE srf01 ALTER COLUMN srf01entidade SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01nome VARCHAR(250) ;
UPDATE srf01 SET srf01nome = ' ' WHERE srf01nome IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01nome SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01ni VARCHAR(20) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01ie VARCHAR(30) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01im VARCHAR(30) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01cep VARCHAR(8) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01endereco VARCHAR(200) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01bairro VARCHAR(50) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01endnumero VARCHAR(50) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01complemento VARCHAR(50) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01uf VARCHAR(2) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01municipio VARCHAR(50) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01dtcanc DATE ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01vlrtotal NUMERIC(16,2) ;
UPDATE srf01 SET srf01vlrtotal = 0 WHERE srf01vlrtotal IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01vlrtotal SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01vlriss NUMERIC(16,2) ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01dfeaprov BIGINT ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01dfecancaprov BIGINT ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01integracaoscf SMALLINT  DEFAULT 0 ;
UPDATE srf01 SET srf01integracaoscf = 0 WHERE srf01integracaoscf IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01integracaoscf SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01integracaogdf SMALLINT  DEFAULT 0 ;
UPDATE srf01 SET srf01integracaogdf = 0 WHERE srf01integracaogdf IS NULL;
ALTER TABLE srf01 ALTER COLUMN srf01integracaogdf SET NOT NULL;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01obs TEXT ;

ALTER TABLE srf01 ADD COLUMN IF NOT EXISTS srf01dtemail DATE ;

DROP INDEX IF EXISTS srf01_uk;
CREATE UNIQUE INDEX srf01_uk ON srf01 (srf01empresa, srf01natureza, srf01numero, COALESCE(srf01serie, 0));
DROP INDEX IF EXISTS srf01_mk;
CREATE INDEX srf01_mk ON srf01 (srf01empresa, srf01natureza, srf01numero, srf01serie);

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011doc BIGINT ;
ALTER TABLE srf011 ALTER COLUMN srf011doc SET NOT NULL;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011item BIGINT ;
ALTER TABLE srf011 ALTER COLUMN srf011item SET NOT NULL;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011descr TEXT ;
UPDATE srf011 SET srf011descr = ' ' WHERE srf011descr IS NULL;
ALTER TABLE srf011 ALTER COLUMN srf011descr SET NOT NULL;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011qtd NUMERIC(16,6) ;
UPDATE srf011 SET srf011qtd = 0 WHERE srf011qtd IS NULL;
ALTER TABLE srf011 ALTER COLUMN srf011qtd SET NOT NULL;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011vlrunit NUMERIC(16,6) ;
UPDATE srf011 SET srf011vlrunit = 0 WHERE srf011vlrunit IS NULL;
ALTER TABLE srf011 ALTER COLUMN srf011vlrunit SET NOT NULL;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011vlrtotal NUMERIC(16,2) ;
UPDATE srf011 SET srf011vlrtotal = 0 WHERE srf011vlrtotal IS NULL;
ALTER TABLE srf011 ALTER COLUMN srf011vlrtotal SET NOT NULL;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011alqiss NUMERIC(4,2) ;

ALTER TABLE srf011 ADD COLUMN IF NOT EXISTS srf011vlriss NUMERIC(16,2) ;

ALTER TABLE srf012 ADD COLUMN IF NOT EXISTS srf012doc BIGINT ;
ALTER TABLE srf012 ALTER COLUMN srf012doc SET NOT NULL;

ALTER TABLE srf012 ADD COLUMN IF NOT EXISTS srf012forma BIGINT ;
ALTER TABLE srf012 ALTER COLUMN srf012forma SET NOT NULL;

ALTER TABLE srf012 ADD COLUMN IF NOT EXISTS srf012parcela INTEGER ;
UPDATE srf012 SET srf012parcela = 0 WHERE srf012parcela IS NULL;
ALTER TABLE srf012 ALTER COLUMN srf012parcela SET NOT NULL;

ALTER TABLE srf012 ADD COLUMN IF NOT EXISTS srf012dtvenc DATE ;
UPDATE srf012 SET srf012dtvenc = '2000-01-01' WHERE srf012dtvenc IS NULL;
ALTER TABLE srf012 ALTER COLUMN srf012dtvenc SET NOT NULL;

ALTER TABLE srf012 ADD COLUMN IF NOT EXISTS srf012valor NUMERIC(16,2) ;
UPDATE srf012 SET srf012valor = 0 WHERE srf012valor IS NULL;
ALTER TABLE srf012 ALTER COLUMN srf012valor SET NOT NULL;

ALTER TABLE srf012 ADD COLUMN IF NOT EXISTS srf012documento BIGINT ;
DO $$ BEGIN BEGIN  ALTER TABLE cas30 ADD CONSTRAINT cas30empativa_cas65_FK FOREIGN KEY (cas30empativa) REFERENCES cas65 (cas65id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE cas30 ADD CONSTRAINT cas30credencial_cas29_FK FOREIGN KEY (cas30credencial) REFERENCES cas29 (cas29id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE cgs18 ADD CONSTRAINT cgs18modeloemail_cgs15_FK FOREIGN KEY (cgs18modeloemail) REFERENCES cgs15 (cgs15id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE cgs38 ADD CONSTRAINT cgs38conta_cgs45_FK FOREIGN KEY (cgs38conta) REFERENCES cgs45 (cgs45id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE cgs40 ADD CONSTRAINT cgs40sup_cgs40_FK FOREIGN KEY (cgs40sup) REFERENCES cgs40 (cgs40id) ON DELETE SET NULL ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE scf02 ADD CONSTRAINT scf02forma_cgs38_FK FOREIGN KEY (scf02forma) REFERENCES cgs38 (cgs38id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE scf02 ADD CONSTRAINT scf02entidade_cgs80_FK FOREIGN KEY (scf02entidade) REFERENCES cgs80 (cgs80id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE scf02 ADD CONSTRAINT scf02lancamento_scf11_FK FOREIGN KEY (scf02lancamento) REFERENCES scf11 (scf11id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE scf021 ADD CONSTRAINT scf021doc_scf02_FK FOREIGN KEY (scf021doc) REFERENCES scf02 (scf02id) ON DELETE CASCADE ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE scf021 ADD CONSTRAINT scf021conta_cgs45_FK FOREIGN KEY (scf021conta) REFERENCES cgs45 (cgs45id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE scf11 ADD CONSTRAINT scf11conta_cgs45_FK FOREIGN KEY (scf11conta) REFERENCES cgs45 (cgs45id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf01 ADD CONSTRAINT srf01natureza_cgs18_FK FOREIGN KEY (srf01natureza) REFERENCES cgs18 (cgs18id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf01 ADD CONSTRAINT srf01entidade_cgs80_FK FOREIGN KEY (srf01entidade) REFERENCES cgs80 (cgs80id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf01 ADD CONSTRAINT srf01dfeaprov_gdf10_FK FOREIGN KEY (srf01dfeaprov) REFERENCES gdf10 (gdf10id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf01 ADD CONSTRAINT srf01dfecancaprov_gdf10_FK FOREIGN KEY (srf01dfecancaprov) REFERENCES gdf10 (gdf10id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf011 ADD CONSTRAINT srf011doc_srf01_FK FOREIGN KEY (srf011doc) REFERENCES srf01 (srf01id) ON DELETE CASCADE ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf011 ADD CONSTRAINT srf011item_cgs50_FK FOREIGN KEY (srf011item) REFERENCES cgs50 (cgs50id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf012 ADD CONSTRAINT srf012doc_srf01_FK FOREIGN KEY (srf012doc) REFERENCES srf01 (srf01id) ON DELETE CASCADE ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf012 ADD CONSTRAINT srf012forma_cgs38_FK FOREIGN KEY (srf012forma) REFERENCES cgs38 (cgs38id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
DO $$ BEGIN BEGIN  ALTER TABLE srf012 ADD CONSTRAINT srf012documento_scf02_FK FOREIGN KEY (srf012documento) REFERENCES scf02 (scf02id) ON DELETE NO ACTION ;  EXCEPTION WHEN duplicate_object THEN RAISE NOTICE 'Constraint already exists, command ignored';  END; END $$;
--- Pós ---
DROP INDEX IF EXISTS cgs50_uk;CREATE UNIQUE INDEX cgs50_uk ON cgs50 (cgs50empresa, cgs50codigo);DROP INDEX IF EXISTS cgs80_uk;CREATE UNIQUE INDEX cgs80_uk ON cgs80 (cgs80empresa, cgs80codigo);

INSERT INTO db_migration (name, time_run) VALUES ('V_00002.sql', NOW());
