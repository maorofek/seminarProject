module com.seminarproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires javafx.media;

    opens com.seminarproject to javafx.fxml;
    exports com.seminarproject;

}