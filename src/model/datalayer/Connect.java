package model.datalayer;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Connect {
    private static final String url = "jdbc:mariadb://localhost";
    private static final String user = "root";
    private static final String password = "";
    private static final String database = "smartsale";

    private static Connection connection;
    private static SQLException fail;

    private static final Alert alert = new Alert(Alert.AlertType.NONE);

    /**
     * Construtor privado para aplicar o design pattern singleton.
     */
    private Connect() {
    }

    /**
     * Obtém uma instância única de conexão utilizando o design pattern
     * singleton.
     *
     * @return Objeto da classe Connection, se a tentativa de conexão com o
     * banco de dados for sucesso. NULL caso contrário.
     */
    public static Connection instance() {
        if (connection == null) {
            try {
                Connect.connection = DriverManager.getConnection(
                    url + "/" + database,
                    user,
                    password
                );
            } catch (SQLException e) {
                Connect.fail = e;
            }
        }

        return connection;
    }

    /**
     * Obtém os erros gerados na tentativa de uma conexão com o banco de dados
     * mal sucedida.
     *
     * @return Objeto da classe SQLException se houver algum erro. NULL caso
     * contrário.
     */
    public static SQLException fail() {
        return fail;
    }

    /**
     * Testa se existe uma conexão com um servidor de banco de dados e a
     * existência do próprio banco.
     *
     * @return TRUE se existir conexão com um servidor e um banco de dados,
     * FALSE caso contrário.
     */
    public static boolean checkDatabase() {
        alert.setTitle("Smart Sale");
        alert.setHeaderText(null);

        ButtonType buttonTypeCancel = new ButtonType(
            "Encerrar programa",
            ButtonBar.ButtonData.CANCEL_CLOSE
        );

        try {
            DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Este programa requer uma conexão ativa com um servidor de banco de dados MySQL/MariaDB.");
            alert.getButtonTypes().setAll(buttonTypeCancel);
            alert.show();

            return false;
        }

        try {
            DriverManager.getConnection(url + "/" + database, user, password);
            return true;
        } catch (SQLException e) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("O banco de dados '" + database + "' não foi encontrado!");

            ButtonType buttonTypeCreateDatabase = new ButtonType(
                "Criar agora",
                ButtonBar.ButtonData.OK_DONE
            );
            alert.getButtonTypes().setAll(
                buttonTypeCreateDatabase,
                buttonTypeCancel
            );

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeCreateDatabase) {
                return createDatabase();
            }

            return false;
        }
    }

    /**
     * Cria um banco de dados, caso não exista.
     *
     * @return TRUE se não houver erro no processo de criação, FALSE caso
     * contrário.
     */
    private static boolean createDatabase() {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + database + "`;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`user`( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(60) NOT NULL, `username` VARCHAR(20) NOT NULL, `password` VARCHAR(20) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`username`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`supplier`( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(100) NOT NULL, `phone` VARCHAR(20) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`name`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`measure_type`( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(20) NOT NULL, `acronym_minimum` VARCHAR(2) NOT NULL, `acronym_maximum` VARCHAR(2) NULL, PRIMARY KEY (`id`), UNIQUE (`name`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`product`( `measure_type_id` INT NOT NULL, `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(60) NOT NULL, `price` DECIMAL(6, 2) NOT NULL DEFAULT 0, `stock` INT NOT NULL DEFAULT 0, `alert_ending` INT NOT NULL DEFAULT 0, PRIMARY KEY (`id`), UNIQUE (`name`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`product_has_supplier`( `product_id` INT NOT NULL, `supplier_id` INT NOT NULL, `id` INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (`id`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`payment_method`( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(30) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`name`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`customer`( `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(60) NOT NULL, `phone` VARCHAR(20) NOT NULL, `zipcode` CHAR(9) NOT NULL, `address` VARCHAR(255) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`sale`( `customer_id` INT NOT NULL, `payment_method_id` INT NOT NULL, `id` INT NOT NULL AUTO_INCREMENT, `price` DECIMAL(6, 2) NULL DEFAULT 0, `date` TIMESTAMP NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `" + database + "`.`sale_item`( `sale_id` INT NOT NULL, `id` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(60) NOT NULL, `price` DECIMAL(6, 2) NOT NULL, `amount` INT NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB DEFAULT CHARSET utf8;");
            statement.executeUpdate("ALTER TABLE `" + database + "`.`product` ADD FOREIGN KEY(`measure_type_id`) REFERENCES `measure_type` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;");
            statement.executeUpdate("ALTER TABLE `" + database + "`.`product_has_supplier` ADD FOREIGN KEY(`product_id`) REFERENCES `product` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE, ADD FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;");
            statement.executeUpdate("ALTER TABLE `" + database + "`.`sale` ADD FOREIGN KEY(`customer_id`) REFERENCES `customer` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE, ADD FOREIGN KEY (`payment_method_id`) REFERENCES `payment_method` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;");
            statement.executeUpdate("ALTER TABLE `" + database + "`.`sale_item` ADD FOREIGN KEY(`sale_id`) REFERENCES `sale` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;");
            statement.executeUpdate("INSERT INTO `" + database + "`.`user`(`name`, `username`, `password`) VALUES ('Giovanni Oliveira', 'giovanni', 'giovanni');");
            statement.executeUpdate("INSERT INTO `" + database + "`.`measure_type`(`name`, `acronym_minimum`, `acronym_maximum`) VALUES ('Grama', 'g', 'kg'), ('Mililitro', 'ml', 'l'), ('Unitário', 'u', NULL);");

            connection.close();

            return true;
        } catch (SQLException e) {
            alert.setContentText("Ocorreu um erro durante a criação do banco de dados!");
            return false;
        }
    }
}
