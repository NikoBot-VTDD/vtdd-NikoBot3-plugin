-- --------------------------------------------------------
-- 主機:                           
-- 伺服器版本:                        10.4.19-MariaDB-1:10.4.19+maria~bionic-log - mariadb.org binary distribution
-- 伺服器作業系統:                    
-- HeidiSQL 版本:                  11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 傾印  資料表 DData.VTDD_CHANNEL 結構
CREATE TABLE IF NOT EXISTS `VTDD_CHANNEL` (
  `Nickname` varchar(20) NOT NULL,
  `Emoji` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `ChannelID` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `MultiLevel` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`Nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 正在傾印表格  DData.VTDD_CHANNEL 的資料：~42 rows (近似值)
/*!40000 ALTER TABLE `VTDD_CHANNEL` DISABLE KEYS */;
INSERT INTO `VTDD_CHANNEL` (`Nickname`, `Emoji`, `ChannelID`, `MultiLevel`) VALUES
	('486', '🚑', 'UCvzGlP9oQwU--Y0r9id_jnA', 0),
	('aki', '🍎', 'UCFTLzh12_nrtzqBPsTCqenA', 0),
	('amelia', '🔎', 'UCyl1z3jo3XHR1riLFKG5UAg', 1),
	('aqua', '⚓️', 'UC1opHUrw8rvnsadT-iGp7Cg', 0),
	('ayame', '😈', 'UC7fk0CB07ly8oSl0aqKkqFg', 0),
	('azki', '🎤', 'UC0TXe_LYZ4scaW2XMyi5_kw', 0),
	('botan', '♌️', 'UCUKD-uaobj9jiqB-VXt71mA', 0),
	('choco', '🍫', 'UC1suqwovbL1kzsoaZgFZLKg', 0),
	('coco', '🐉', 'UCS9uQI-jC3DE0L4IpXyvr6w', 0),
	('flare', '🔥', 'UCvInZx9h3jC2JzsIzoOebWg', 0),
	('fubuki', '🌽', 'UCdn5BQ06XqgXoAxIhbqw5Rg', 0),
	('gura', '🔱', 'UCoSrY_IQQVpmIRZ9Xf-y93g', 1),
	('haato', '❤️', 'UC1CfXB_kRs3C-zaeTG3oGyg', 1),
	('ina', '🐙', 'UCMwGHR0BTZuLsmjY_NT5Pwg', 1),
	('kanata', '💫', 'UCZlDXzGoo7d44bwdNObFacg', 0),
	('kiara', '🐔', 'UCHsx4Hqa-1ORjQTh9TYDhww', 0),
	('korone', '🥐', 'UChAnqc_AY5_I3Px5dig3X1Q', 0),
	('lamy', '☃', 'UCFKOVgVbGmX65RxO3EtH3iw', 0),
	('lofi', '🎨', 'UCAoy6rzhSf4ydcYjJw3WoVg', 0),
	('luna', '🍬', 'UCa9Y57gfeY0Zro_noHRVrnw', 1),
	('marine', '🏴‍☠️', 'UCCzUftO8KOVkV4wQG1vkUvg', 0),
	('matsuri', '🏮', 'UCQ0UDLQCjY0rmuxCDE38FGg', 1),
	('mel', '🌟', 'UCD8HOxPs4Xvsm8H0ZxXGiBw', 1),
	('melfissa', '🍂', 'UC727SQYUvx5pDDGQpTICNWg', 0),
	('miko', '💮', 'UC-hM6YJuNYVAmUWxeIr9FeA', 0),
	('mio', '🌲', 'UCp-5t9SrOQwXMU7iIjQfARg', 0),
	('moona', '🔮', 'UCP0BspO_AMEe3aQqqpo89Dg', 0),
	('mori', '💀', 'UCL_qhgtOy0dy1Agp8vkySQg', 1),
	('nene', '🥟', 'UCAWSyEs_Io8MtpY3m-zqILA', 0),
	('noel', '⚔', 'UCdyqAaZDKHXg4Ahi7VENThQ', 0),
	('okayu', '🍙', 'UCvaTdHTWBGv3MKj3KVqJVCw', 0),
	('ollie', '🧟', 'UCYz_5n-uDuChHtLo7My1HnQ', 0),
	('pekora', '👯', 'UC1DCedRgGHBdm81E1llLhOQ', 0),
	('polka', '🎪', 'UCK9V2B22uJYu3N7eR_BT9QA', 0),
	('reine', '🦚', 'UChgTyjG-pdNvxxhdsXfHQ5Q', 0),
	('risu', '🐿', 'UCOyYb1c43VlX9rc_lT6NKQw', 0),
	('roboco', '🤖', 'UCDqI2jOz0weumE8s7paEk6g', 1),
	('rushia', '🦋', 'UCl_gCybOJRIgOXw6Qb4qJzQ', 0),
	('shion', '🌙', 'UCXTpFs_3PqI41qX2d9tL2Rw', 0),
	('suisei', '☄️', 'UC5CwaMl1eIgY8h02uZw7u8A', 1),
	('towa', '👾', 'UC1uv2Oq6kNxgATlCiez59hw', 0),
	('watame', '🐑', 'UCqm3BQLlJfvkTsX_hvm0UmA', 0);
/*!40000 ALTER TABLE `VTDD_CHANNEL` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
