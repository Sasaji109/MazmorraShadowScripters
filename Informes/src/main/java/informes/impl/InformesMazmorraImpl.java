package informes.impl;

import game.character.Wizard;
import game.dungeon.Room;
import game.object.Item;
import informes.InformesMazmorra;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class InformesMazmorraImpl implements InformesMazmorra {
    @Override
    public void HistorialAcciones(List<String> acciones) {
        try{
            //build doc
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //build structure
            Element rootElement = document.createElement("historial");
            document.appendChild(rootElement);

            for (String line : acciones){
                Element accion = document.createElement("accion");
                rootElement.appendChild(accion);
                accion.setTextContent(line);
            }

            //write xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            Path path = Paths.get("xml/acciones.xml");

            transformer.transform(new DOMSource(document), new StreamResult(Files.newOutputStream(path)));

            cargar("xml/acciones.xml", "reports/AccionesReport.jrxml","/historial/accion" );


        } catch (ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void CeldasVisitadas(List<Room> celdas) {
        try{
            //build doc
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //build structure
            Element rootElement = document.createElement("historial");
            document.appendChild(rootElement);

            for (Room room : celdas){
                Element celda = document.createElement("celda");
                rootElement.appendChild(celda);

                Element id = document.createElement("id");
                celda.appendChild(id);
                id.setTextContent(String.valueOf(room.getID()));

                Element description = document.createElement("description");
                celda.appendChild(description);
                description.setTextContent(room.getDescription());

                Element salida = document.createElement("salida");
                celda.appendChild(salida);
                salida.setTextContent(String.valueOf(room.isExit()));

                Element crystalfarm = document.createElement("crystal_farm");
                celda.appendChild(crystalfarm);
                crystalfarm.setTextContent(String.valueOf(room.getContainer().getValue()));

                if (room.getCreature()!= null){
                    Element creature = document.createElement("creature");
                    celda.appendChild(creature);
                    creature.setTextContent(room.getCreature().getDomain().toString());
                }

            }

            //write xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            Path path = Paths.get("xml/celdasVisitadas.xml");

            transformer.transform(new DOMSource(document), new StreamResult(Files.newOutputStream(path)));

            cargar("xml/celdasVisitadas.xml", "reports/RepCeldasVisitadas.jrxml","/historial/celda" );


        } catch (ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void HojaPersonaje(Wizard personaje) { /*

        try{
            //build doc
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //root
            Element rootElement = document.createElement("personaje");
            document.appendChild(rootElement);

            Element name = document.createElement("nombre");
            rootElement.appendChild(name);
            name.setTextContent(personaje.getName());

            //vida
            Element vida = document.createElement("vida");
            rootElement.appendChild(vida);
            vida.setTextContent(String.valueOf(personaje.getLife()));

            //energia
            Element energia = document.createElement("energia");
            rootElement.appendChild(energia);
            energia.setTextContent(String.valueOf(personaje.getEnergy()));

            //wearables
            Element wearables = document.createElement("wearables");
            rootElement.appendChild(wearables);

            if (personaje.getWearables().getItems().isEmpty()){
                wearables.setTextContent("empty");
            } else {
                Element items = document.createElement("items");
                wearables.appendChild(items);

                for (Item i : personaje.getWearables().getItems()){
                    Element item = document.createElement("item");
                    items.appendChild(item);

                    Element type = document.createElement("type");
                    item.appendChild(type);
                    type.setTextContent(i.getClass().getSimpleName());

                    Element element = document.createElement("element");
                    item.appendChild(element);
                    element.setTextContent(i.getDomain().name());

                    Element value = document.createElement("value");
                    item.appendChild(value);
                    value.setTextContent(String.valueOf(i.getValue()));
                }
            }

            //carrier
            Element carrier = document.createElement("crystal_carrier");
            rootElement.appendChild(carrier);

            if (personaje.getCrystalCarrier().getItems().isEmpty()){
                carrier.setTextContent("empty");
            } else {
                Element crystals = document.createElement("crystals");
                carrier.appendChild(crystals);

                for(Item i : personaje.getCrystalCarrier().getItems()){
                    Element crystal = document.createElement("crystal");
                    crystals.appendChild(crystal);

                    Element singa = document.createElement("singa");
                    crystal.appendChild(singa);
                    singa.setTextContent(String.valueOf(i.getValue()));
                }
            }


            //jewelry bag
            Element jewelry = document.createElement("jewelry_bag");
            rootElement.appendChild(jewelry);

            if (personaje.getJewelryBag().getItems().isEmpty()){
                jewelry.setTextContent("empty");
            }else {
                Element jewels = document.createElement("jewels");
                jewelry.appendChild(jewels);

                for(Item i : personaje.getJewelryBag().getItems()){
                    Element jewel = document.createElement("jewel");
                    jewels.appendChild(jewel);

                    Element type = document.createElement("type");
                    jewel.appendChild(type);
                    type.setTextContent(i.getClass().getSimpleName());

                    Element element = document.createElement("element");
                    jewel.appendChild(element);
                    element.setTextContent(i.getDomain().name());

                    Element value = document.createElement("value");
                    jewel.appendChild(value);
                    value.setTextContent(String.valueOf(i.getValue()));
                }
            }

            //write xml
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            Path path = Paths.get("xml/personaje.xml");

            transformer.transform(new DOMSource(document), new StreamResult(Files.newOutputStream(path)));

            //cargar
            //todo meter path del modelo y query
            cargar("xml/personaje.xml", "reports/PersonajeReport","/personaje" );


        } catch (ParserConfigurationException | IOException | TransformerException e) {
            throw new RuntimeException(e);
        } */
    }

    public void cargar(String datos, String modelo,String query){
        try {
            InputStream inputStream = InformesMazmorraImpl.class.getClassLoader().getResourceAsStream(datos);
            InputStream inputStream2 = InformesMazmorraImpl.class.getClassLoader().getResourceAsStream(modelo);
            JRXmlDataSource dataSource = new JRXmlDataSource(inputStream, query);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream2);
            JasperPrint jp = JasperFillManager.fillReport(jasperReport, null, dataSource);
            JasperViewer.viewReport(jp, false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

}
