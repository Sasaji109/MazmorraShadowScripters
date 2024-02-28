module Save {
    exports saveTXT;
    exports saveXML;
    exports saveTXT.saveTXTImpl;
    exports saveXML.saveXMLImpl;
    requires java.xml;
    requires game;
}