-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 30, 2023 at 01:13 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `krs_database`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(200) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`username`, `password`) VALUES
('admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `dosen`
--

CREATE TABLE `dosen` (
  `nip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nama_dosen` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telepon` varchar(14) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `alamat` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dosen`
--

INSERT INTO `dosen` (`nip`, `nama_dosen`, `telepon`, `alamat`) VALUES
('111222333', 'Alice Lee', '08765432100', 'Jalan Fantastis No. 101'),
('123456789', 'John Doe', '08123456789', 'Jalan Contoh No. 123'),
('444555666', 'David Wang', '08123456789', 'Jalan Unik No. 246'),
('654321987', 'Bob Johnson', '08111223344', 'Jalan Misterius No. 789'),
('987654321', 'Jane Smith', '08567890123', 'Jalan Ajaib No. 456');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal`
--

CREATE TABLE `jadwal` (
  `kode_jadwal` varchar(10) NOT NULL,
  `kode_makul` varchar(6) NOT NULL,
  `nip_dosen` varchar(18) NOT NULL,
  `kode_kelas` varchar(9) NOT NULL,
  `semester_jadwal` enum('Semester 1','Semester 2','Semester 3','Semester 4','Semester 5','Semester 6','Semester 7','Semester 8') NOT NULL,
  `jam` varchar(11) NOT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `jadwal`
--

INSERT INTO `jadwal` (`kode_jadwal`, `kode_makul`, `nip_dosen`, `kode_kelas`, `semester_jadwal`, `jam`, `hari`) VALUES
('J1', 'TI101', '123456789', 'TI1A', 'Semester 1', '08:00-10:00', 'Senin'),
('J2', 'TI201', '987654321', 'TI1A', 'Semester 1', '10:00-12:00', 'Selasa'),
('J3', 'TI301', '654321987', 'TI2A', 'Semester 3', '13:00-15:00', 'Rabu'),
('J4', 'TI401', '111222333', 'TI2A', 'Semester 3', '15:00-17:00', 'Kamis'),
('J5', 'TI501', '444555666', 'TI3A', 'Semester 5', '08:00-10:00', 'Jumat');

-- --------------------------------------------------------

--
-- Table structure for table `kelas`
--

CREATE TABLE `kelas` (
  `kode_kelas` varchar(9) NOT NULL,
  `nama_kelas` varchar(2) NOT NULL,
  `semester_kelas` enum('Semester 1','Semester 2','Semester 3','Semester 4','Semester 5','Semester 6','Semester 7','Semester 8') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `kelas`
--

INSERT INTO `kelas` (`kode_kelas`, `nama_kelas`, `semester_kelas`) VALUES
('TI1A', '1A', 'Semester 1'),
('TI1B', '1B', 'Semester 1'),
('TI2A', '2A', 'Semester 2'),
('TI2B', '2B', 'Semester 2'),
('TI3A', '3A', 'Semester 3'),
('TI3B', '3B', 'Semester 3'),
('TI4A', '4A', 'Semester 4'),
('TI4B', '4B', 'Semester 4'),
('TI5A', '5A', 'Semester 5'),
('TI5B', '5B', 'Semester 5');

-- --------------------------------------------------------

--
-- Table structure for table `krs`
--

CREATE TABLE `krs` (
  `id_krs` varchar(200) NOT NULL,
  `id_mhs` int NOT NULL,
  `kode_jadwal` varchar(200) NOT NULL,
  `tahun_ajar` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `krs`
--

INSERT INTO `krs` (`id_krs`, `id_mhs`, `kode_jadwal`, `tahun_ajar`) VALUES
('1', 1, 'J1', '2022/2023'),
('2', 2, 'J2', '2022/2023'),
('3', 3, 'J3', '2022/2023'),
('4', 4, 'J4', '2022/2023'),
('5', 5, 'J5', '2022/2023'),
('KS0001', 1, 'J2', '2023/2024');

-- --------------------------------------------------------

--
-- Table structure for table `makul`
--

CREATE TABLE `makul` (
  `kode_makul` varchar(6) NOT NULL,
  `makul` varchar(50) NOT NULL,
  `sks` int NOT NULL,
  `semester` enum('Semester 1','Semester 2','Semester 3','Semester 4','Semester 5','Semester 6','Semester 7','Semester 8') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `makul`
--

INSERT INTO `makul` (`kode_makul`, `makul`, `sks`, `semester`) VALUES
('TI101', 'Pengantar Informatika', 3, 'Semester 1'),
('TI102', 'Matematika Dasar', 4, 'Semester 1'),
('TI201', 'Algoritma dan Struktur Data', 4, 'Semester 3'),
('TI202', 'Pemrograman Web', 3, 'Semester 3'),
('TI301', 'Basis Data', 4, 'Semester 5'),
('TI302', 'Sistem Operasi', 3, 'Semester 5'),
('TI401', 'Pemrograman Lanjut', 4, 'Semester 7'),
('TI402', 'Jaringan Komputer', 3, 'Semester 7'),
('TI501', 'Kecerdasan Buatan', 3, 'Semester 8'),
('TI502', 'Proyek Akhir', 6, 'Semester 8');

-- --------------------------------------------------------

--
-- Table structure for table `mhs`
--

CREATE TABLE `mhs` (
  `id_mhs` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `j_kelamin` enum('Laki-laki','Perempuan') NOT NULL,
  `prog_studi` enum('S1 Teknik Informatika','S1 Sistem Informasi') NOT NULL,
  `tgl_lahir` date NOT NULL,
  `tempat_lahir` varchar(50) NOT NULL,
  `password` varchar(200) DEFAULT NULL,
  `foto` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mhs`
--

INSERT INTO `mhs` (`id_mhs`, `npm`, `nama`, `j_kelamin`, `prog_studi`, `tgl_lahir`, `tempat_lahir`, `password`, `foto`) VALUES
(1, '180101001', 'John Doe', 'Laki-laki', 'S1 Teknik Informatika', '1999-05-15', 'Jakarta', 'password123', 0x6a6f686e5f646f652e6a7067),
(2, '180101002', 'Jane Smith', 'Perempuan', 'S1 Sistem Informasi', '2000-02-20', 'Bandung', 'pass456', 0x6a616e655f736d6974682e6a7067),
(3, '180101003', 'Bob Johnson', 'Laki-laki', 'S1 Teknik Informatika', '1998-08-10', 'Surabaya', 'pass789', 0x626f625f6a6f686e736f6e2e6a7067),
(4, '180101004', 'Alice Lee', 'Perempuan', 'S1 Sistem Informasi', '2001-01-25', 'Yogyakarta', '123abc', 0x616c6963655f6c65652e6a7067),
(5, '180101005', 'David Wang', 'Laki-laki', 'S1 Teknik Informatika', '1999-11-30', 'Medan', 'passxyz', 0x64617669645f77616e672e6a7067);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`username`);

--
-- Indexes for table `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`nip`);

--
-- Indexes for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD PRIMARY KEY (`kode_jadwal`),
  ADD KEY `kode_makul` (`kode_makul`),
  ADD KEY `nip_dosen` (`nip_dosen`),
  ADD KEY `kode_kelas` (`kode_kelas`);

--
-- Indexes for table `kelas`
--
ALTER TABLE `kelas`
  ADD PRIMARY KEY (`kode_kelas`);

--
-- Indexes for table `krs`
--
ALTER TABLE `krs`
  ADD PRIMARY KEY (`id_krs`),
  ADD KEY `id_mhs` (`id_mhs`),
  ADD KEY `kode_jadwal` (`kode_jadwal`),
  ADD KEY `tahun_ajar` (`tahun_ajar`);

--
-- Indexes for table `makul`
--
ALTER TABLE `makul`
  ADD PRIMARY KEY (`kode_makul`);

--
-- Indexes for table `mhs`
--
ALTER TABLE `mhs`
  ADD PRIMARY KEY (`id_mhs`),
  ADD UNIQUE KEY `id_mhs` (`id_mhs`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mhs`
--
ALTER TABLE `mhs`
  MODIFY `id_mhs` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `jadwal`
--
ALTER TABLE `jadwal`
  ADD CONSTRAINT `jadwal_ibfk_1` FOREIGN KEY (`kode_makul`) REFERENCES `makul` (`kode_makul`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_ibfk_2` FOREIGN KEY (`nip_dosen`) REFERENCES `dosen` (`nip`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwal_ibfk_3` FOREIGN KEY (`kode_kelas`) REFERENCES `kelas` (`kode_kelas`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `krs`
--
ALTER TABLE `krs`
  ADD CONSTRAINT `krs_ibfk_1` FOREIGN KEY (`id_mhs`) REFERENCES `mhs` (`id_mhs`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `krs_ibfk_2` FOREIGN KEY (`kode_jadwal`) REFERENCES `jadwal` (`kode_jadwal`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
