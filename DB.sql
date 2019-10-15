-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  jeu. 03 oct. 2019 à 20:56
-- Version du serveur :  10.1.37-MariaDB
-- Version de PHP :  7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `sandb`
--

-- --------------------------------------------------------

--
-- Structure de la table `employers`
--

CREATE TABLE `employers` (
  `emp_id` int(11) NOT NULL,
  `fullname` varchar(100) NOT NULL,
  `telephone` int(12) DEFAULT NULL,
  `salary` int(11) NOT NULL,
  `joined_date` date NOT NULL,
  `admin` int(1) NOT NULL DEFAULT '0',
  `active` int(1) NOT NULL DEFAULT '1',
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `emp_image` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `employers`
--

INSERT INTO `employers` (`emp_id`, `fullname`, `telephone`, `salary`, `joined_date`, `admin`, `active`, `username`, `password`, `emp_image`) VALUES
(7, 'Admin admin', 555555555, 2500, '2019-10-03', 1, 1, 'admin', 'admin', '');

-- --------------------------------------------------------

--
-- Structure de la table `employer_payment`
--

CREATE TABLE `employer_payment` (
  `empay_id` int(11) NOT NULL,
  `emp_id` int(11) DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `pay_date` date NOT NULL,
  `absent` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `payment`
--

CREATE TABLE `payment` (
  `pay_id` int(11) NOT NULL,
  `paid` int(11) NOT NULL,
  `rest` int(11) NOT NULL,
  `pDate` date NOT NULL,
  `sell_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `product`
--

CREATE TABLE `product` (
  `prod_id` int(11) NOT NULL,
  `reference` varchar(50) NOT NULL,
  `size` int(11) NOT NULL,
  `brand` varchar(50) NOT NULL,
  `price` int(11) NOT NULL,
  `add_date` date NOT NULL,
  `category` varchar(50) NOT NULL DEFAULT 'Other',
  `color` varchar(50) NOT NULL,
  `imageURL` varchar(255) DEFAULT NULL,
  `sold` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `sell`
--

CREATE TABLE `sell` (
  `sell_id` int(11) NOT NULL,
  `sellPrice` int(11) NOT NULL,
  `sell_date` date NOT NULL,
  `emp_id` int(11) DEFAULT NULL,
  `prod_id` int(11) NOT NULL,
  `hasPayment` int(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `employers`
--
ALTER TABLE `employers`
  ADD PRIMARY KEY (`emp_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Index pour la table `employer_payment`
--
ALTER TABLE `employer_payment`
  ADD PRIMARY KEY (`empay_id`),
  ADD KEY `fk_recordEmp` (`emp_id`);

--
-- Index pour la table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`pay_id`),
  ADD KEY `fk_paySell` (`sell_id`);

--
-- Index pour la table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`prod_id`);

--
-- Index pour la table `sell`
--
ALTER TABLE `sell`
  ADD PRIMARY KEY (`sell_id`),
  ADD KEY `fk_sellref` (`prod_id`),
  ADD KEY `fk_sellemp` (`emp_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `employers`
--
ALTER TABLE `employers`
  MODIFY `emp_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `employer_payment`
--
ALTER TABLE `employer_payment`
  MODIFY `empay_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `payment`
--
ALTER TABLE `payment`
  MODIFY `pay_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `product`
--
ALTER TABLE `product`
  MODIFY `prod_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT pour la table `sell`
--
ALTER TABLE `sell`
  MODIFY `sell_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `employer_payment`
--
ALTER TABLE `employer_payment`
  ADD CONSTRAINT `fk_recordEmp` FOREIGN KEY (`emp_id`) REFERENCES `employers` (`emp_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `fk_paySell` FOREIGN KEY (`sell_id`) REFERENCES `sell` (`sell_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `sell`
--
ALTER TABLE `sell`
  ADD CONSTRAINT `fk_sellemp` FOREIGN KEY (`emp_id`) REFERENCES `employers` (`emp_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sellref` FOREIGN KEY (`prod_id`) REFERENCES `product` (`prod_id`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
