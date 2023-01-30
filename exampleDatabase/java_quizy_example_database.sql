-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 29 Sty 2023, 19:53
-- Wersja serwera: 10.4.25-MariaDB
-- Wersja PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `java_quizy_example_database`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `answers`
--

CREATE TABLE `answers` (
  `id_answer` int(11) NOT NULL,
  `answer` varchar(512) NOT NULL,
  `is_correct` bit(1) NOT NULL,
  `id_question` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `answers`
--

INSERT INTO `answers` (`id_answer`, `answer`, `is_correct`, `id_question`) VALUES
(1, 'Answer 1', b'1', 1),
(2, 'Answer 2', b'0', 1),
(3, 'Answer 3', b'0', 1),
(4, 'Answer 4', b'0', 1),
(5, 'Green', b'1', 2),
(6, 'Red', b'0', 2),
(7, 'Blue', b'0', 2),
(8, '2', b'1', 3),
(9, '1', b'0', 3),
(10, 'True', b'1', 4),
(11, 'False', b'0', 4),
(12, 'Cucumber', b'1', 5),
(13, 'Tomato', b'1', 5),
(14, 'Potato', b'1', 5),
(15, 'Lettuce', b'1', 5),
(16, 'Banana', b'0', 5),
(17, 'Apple', b'0', 5),
(18, 'Pizza', b'0', 5),
(19, 'True', b'1', 6),
(20, 'False', b'0', 6),
(21, '4', b'1', 7),
(22, '2', b'0', 7),
(23, '5', b'0', 7),
(24, '3', b'0', 7),
(25, 'Dog', b'1', 8),
(26, 'Cat', b'1', 8),
(27, 'Horse', b'1', 8),
(28, 'Car', b'0', 8),
(29, 'Warsaw', b'1', 9),
(30, 'London', b'0', 9),
(31, 'Washinton', b'0', 9),
(32, 'Rzeszów', b'0', 9),
(33, ' Antarctic blue whale', b'1', 10),
(34, 'Elephant', b'0', 10),
(35, 'Grizzly Bear', b'0', 10),
(36, 'Giraffe', b'0', 10),
(37, '1/2', b'1', 11),
(38, '3/4', b'0', 11),
(39, '5/6', b'0', 11),
(40, '2/3', b'0', 11);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `questions`
--

CREATE TABLE `questions` (
  `id_question` int(11) NOT NULL,
  `question` varchar(1024) DEFAULT NULL,
  `answer_count` tinyint(4) DEFAULT NULL,
  `id_type` int(11) NOT NULL,
  `id_quiz` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `questions`
--

INSERT INTO `questions` (`id_question`, `question`, `answer_count`, `id_type`, `id_quiz`) VALUES
(1, 'Question 1', 4, 1, 1),
(2, 'What color is the grass?', 3, 1, 1),
(3, 'What is the square root of 4?', 2, 1, 1),
(4, 'Do you like this app ?', 2, 2, 1),
(5, 'Selecte Vegetables', 7, 3, 1),
(6, 'Is java a good programming language', 2, 2, 2),
(7, 'Whats 2+2 ?', 4, 1, 2),
(8, 'Select animals', 4, 3, 2),
(9, 'What is the capital of Poland', 4, 1, 3),
(10, 'What is the biggest mammal', 4, 1, 3),
(11, '0.5 is in other words', 4, 1, 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `quizes`
--

CREATE TABLE `quizes` (
  `id_quiz` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `quizes`
--

INSERT INTO `quizes` (`id_quiz`, `name`, `id_user`) VALUES
(1, 'ExampleQuiz1', 1),
(2, 'Example Quiz 2', 1),
(3, 'Are you smart', 2);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `types`
--

CREATE TABLE `types` (
  `id_type` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `types`
--

INSERT INTO `types` (`id_type`, `name`) VALUES
(1, 'Simple Question'),
(2, 'True or False Question'),
(3, 'Multiselection Question');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `login` varchar(50) NOT NULL,
  `passw` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`id_user`, `login`, `passw`) VALUES
(1, 'admin', 'admin'),
(2, 'user1', 'passwd');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `answers`
--
ALTER TABLE `answers`
  ADD PRIMARY KEY (`id_answer`),
  ADD KEY `id_question` (`id_question`);

--
-- Indeksy dla tabeli `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`id_question`),
  ADD KEY `id_type` (`id_type`),
  ADD KEY `id_quiz` (`id_quiz`);

--
-- Indeksy dla tabeli `quizes`
--
ALTER TABLE `quizes`
  ADD PRIMARY KEY (`id_quiz`),
  ADD KEY `id_user` (`id_user`);

--
-- Indeksy dla tabeli `types`
--
ALTER TABLE `types`
  ADD PRIMARY KEY (`id_type`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `answers`
--
ALTER TABLE `answers`
  MODIFY `id_answer` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT dla tabeli `questions`
--
ALTER TABLE `questions`
  MODIFY `id_question` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT dla tabeli `quizes`
--
ALTER TABLE `quizes`
  MODIFY `id_quiz` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `types`
--
ALTER TABLE `types`
  MODIFY `id_type` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `answers`
--
ALTER TABLE `answers`
  ADD CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`id_question`) REFERENCES `questions` (`id_question`) ON DELETE CASCADE;

--
-- Ograniczenia dla tabeli `questions`
--
ALTER TABLE `questions`
  ADD CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`id_type`) REFERENCES `types` (`id_type`),
  ADD CONSTRAINT `questions_ibfk_2` FOREIGN KEY (`id_quiz`) REFERENCES `quizes` (`id_quiz`) ON DELETE CASCADE;

--
-- Ograniczenia dla tabeli `quizes`
--
ALTER TABLE `quizes`
  ADD CONSTRAINT `quizes_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
