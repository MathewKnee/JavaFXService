-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 18 Lut 2021, 23:02
-- Wersja serwera: 10.4.14-MariaDB
-- Wersja PHP: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `serwis`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `assigned_orders`
--

CREATE TABLE `assigned_orders` (
  `employee_ID` int(11) NOT NULL,
  `order_ID` int(11) NOT NULL,
  `taken` date NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `assigned_orders`
--

INSERT INTO `assigned_orders` (`employee_ID`, `order_ID`, `taken`) VALUES
(2, 4, '2020-11-29'),
(2, 3, '2020-11-29'),
(2, 1, '2020-11-29'),
(2, 10, '2020-12-11'),
(2, 14, '2020-12-11'),
(2, 13, '2021-02-14'),
(2, 12, '2021-02-17'),
(2, 2, '2021-02-18'),
(2, 21, '2021-02-18'),
(2, 15, '2021-02-18');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `order_queue`
--

CREATE TABLE `order_queue` (
  `ID` int(11) NOT NULL,
  `device` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL,
  `placed` date NOT NULL DEFAULT current_timestamp(),
  `client_id` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `description` text COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `order_queue`
--

INSERT INTO `order_queue` (`ID`, `device`, `placed`, `client_id`, `state`, `description`) VALUES
(2, 'PC', '2020-11-28', 1, 2, 'PC'),
(3, 'Laptop', '2020-11-28', 1, 1, 'Laptop'),
(10, 'Laptop', '2020-11-29', 3, 1, 'Blue Screen'),
(13, 'Smartphone', '2020-12-11', 3, 1, 'Broken Button'),
(15, 'PC', '2020-12-11', 1, 2, 'Motherboard'),
(17, 'TV', '2021-02-17', 1, 0, 'Broken screen'),
(18, 'Laptop', '2021-02-18', 3, 0, 'Trackpad'),
(19, 'TV', '2021-02-18', 3, 0, 'Remote'),
(20, 'TV', '2021-02-18', 3, 0, 'Remote'),
(21, 'Phone', '2021-02-18', 3, 1, 'Broken button');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `ID` int(10) UNSIGNED NOT NULL,
  `username` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL,
  `passwd` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL,
  `type` int(11) NOT NULL,
  `name` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL,
  `surname` varchar(25) COLLATE utf8mb4_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`ID`, `username`, `passwd`, `type`, `name`, `surname`) VALUES
(1, 'admin', 'zaq1@wsx', 0, 'Jack', 'Gray'),
(2, 'emp_test', 'test', 1, 'Jan', 'Kowalski'),
(3, 'client_test', 'test', 2, 'Jacek', 'Nowak'),
(9, 'emp_test2', 'test', 1, 'Adam', 'Black');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `order_queue`
--
ALTER TABLE `order_queue`
  ADD PRIMARY KEY (`ID`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `order_queue`
--
ALTER TABLE `order_queue`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
