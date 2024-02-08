module GUI {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.logging.log4j;
    requires io.vavr;
    requires java.sql;
    requires java.se;
    requires jakarta.xml.bind;
    requires jakarta.annotation;
    requires jakarta.inject;
    requires jakarta.cdi;
    requires game;

    exports gui.main;
    exports gui.pantallas.principal;
    exports gui.pantallas.inicio;
    exports gui.pantallas.common;

    opens gui;
    opens gui.main;
    opens gui.pantallas.principal;
    opens gui.pantallas.inicio;
}