package saveXML.saveXMLImpl;

import game.character.Wizard;
import game.conditions.*;
import game.demiurge.Demiurge;
import game.dungeon.Dungeon;
import game.dungeon.Home;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import saveXML.SaveXML;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;


public class saveXMLImp implements SaveXML {
    @Override
    public void saveDemiurge(Demiurge demiurgeClass, String filePath) {
        //ejemplo de filePath  = "C:\\Users\\2dam\\Desktop\\data\\result.xml"
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element demiurge = doc.createElement("demiurge");
            doc.appendChild(demiurge);

            Element dungeon = doc.createElement("dungeon");
            demiurge.appendChild(dungeon);

            Element home = doc.createElement("home");
            demiurge.appendChild(home);

            Element wizard = doc.createElement("wizard");
            demiurge.appendChild(wizard);

            Element condtions = doc.createElement("condtions");
            demiurge.appendChild(condtions);

            Element day = doc.createElement("day");
            day.appendChild(doc.createTextNode(String.valueOf(demiurgeClass.getDay())));
            demiurge.appendChild(day);


            guardarHome(home, doc, demiurgeClass.getHome());
            guardarDungeon(dungeon, doc, demiurgeClass.getDungeon());
            guardarWizard(wizard, doc, demiurgeClass.getWizard());
            guardarConditions(condtions, doc, demiurgeClass.getEndChecker().getConditions());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            File outputFile = new File(filePath);


            try (OutputStream os = new FileOutputStream(outputFile)) {
                transformer.transform(new DOMSource(doc), new StreamResult(os));
                System.out.println("XML content successfully written to " + outputFile.getAbsolutePath());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private void guardarHome(Element homeElement, Document doc, Home home) throws TransformerException { /*
        Element description = doc.createElement("description");
        homeElement.appendChild(description);
        description.appendChild(doc.createTextNode(home.getDescription()));

        Element comfort = doc.createElement("comfort");
        homeElement.appendChild(comfort);
        comfort.appendChild(doc.createTextNode(String.valueOf(home.getComfort())));

        Element singa = doc.createElement("singa");
        homeElement.appendChild(singa);

        Element singaCapacity = doc.createElement("singaCapacity");
        singa.appendChild(singaCapacity);
        singaCapacity.appendChild(doc.createTextNode(String.valueOf(home.getSinga())));

        Element singaMaxCapacity = doc.createElement("singaMaxCapacity");
        singa.appendChild(singaMaxCapacity);
        singaMaxCapacity.appendChild(doc.createTextNode(String.valueOf(home.getMaxSinga())));

        Element chest = doc.createElement("chest");
        homeElement.appendChild(chest);

        Element chestValue = doc.createElement("chestValue");
        chest.appendChild(chestValue);
        chestValue.appendChild(doc.createTextNode(String.valueOf(home.getContainer().getValue())));

        Element items = doc.createElement("items");
        chest.appendChild(items);

        Iterator<Item> iterator = home.getContainer().iterator();

        iterator.forEachRemaining(itemClass -> {
            Element item = doc.createElement("item");
            items.appendChild(item);

            Element type = doc.createElement("type");
            if (itemClass instanceof Weapon) {
                type.appendChild(doc.createTextNode("weapon"));
            } else if (itemClass instanceof Necklace) {
                type.appendChild(doc.createTextNode("necklace"));
            } else if (itemClass instanceof Ring) {
                type.appendChild(doc.createTextNode("ring"));
            }
            item.appendChild(type);

            Element element = doc.createElement("element");
            item.appendChild(element);
            element.appendChild(doc.createTextNode(itemClass.getDomain().toString()));

            Element value = doc.createElement("value");
            item.appendChild(value);
            value.appendChild(doc.createTextNode(String.valueOf(itemClass.getValue())));

        });

        Element library = doc.createElement("library");
        homeElement.appendChild(library);

        Element spells = doc.createElement("spells");
        library.appendChild(spells);

        Iterator<Spell> spellsIterator = home.getLibrary().iterator();

        spellsIterator.forEachRemaining(spellClass -> {
            Element spell = doc.createElement("spell");
            spells.appendChild(spell);

            Element element = doc.createElement("element");
            spell.appendChild(element);
            element.appendChild(doc.createTextNode(spellClass.getDomain().toString()));

            Element level = doc.createElement("level");
            spell.appendChild(level);
            level.appendChild(doc.createTextNode(String.valueOf(spellClass.getLevel())));
        });

*/
    }

    private void guardarWizard(Element wizardElement, Document doc, Wizard wizard) throws ParserConfigurationException {
/*
        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(wizard.getName()));
        wizardElement.appendChild(name);

        Element currentLife = doc.createElement("currentLife");
        currentLife.appendChild(doc.createTextNode(String.valueOf(wizard.getLife())));
        wizardElement.appendChild(currentLife);

        Element maxLife = doc.createElement("maxLife");
        maxLife.appendChild(doc.createTextNode(String.valueOf(wizard.getLifeMax())));
        wizardElement.appendChild(maxLife);

        Element currentEnergy = doc.createElement("currentEnergy");
        currentEnergy.appendChild(doc.createTextNode(String.valueOf(wizard.getEnergy())));
        wizardElement.appendChild(currentEnergy);

        Element maxEnergy = doc.createElement("maxEnergy");
        maxEnergy.appendChild(doc.createTextNode(String.valueOf(wizard.getEnergyMax())));
        wizardElement.appendChild(maxEnergy);

        Element wereables = doc.createElement("wereables");
        wizardElement.appendChild(wereables);

        Element maxWeapons = doc.createElement("maxWeapons");
        maxWeapons.appendChild(doc.createTextNode(String.valueOf(((Wearables) wizard.getWearables()).getWeaponsMAX())));
        wereables.appendChild(maxWeapons);

        Element maxNecklaces = doc.createElement("maxNecklaces");
        maxNecklaces.appendChild(doc.createTextNode(String.valueOf(((Wearables) wizard.getWearables()).getNecklacesMAX())));
        wereables.appendChild(maxNecklaces);

        Element maxRings = doc.createElement("maxRings");
        maxRings.appendChild(doc.createTextNode(String.valueOf(((Wearables) wizard.getWearables()).getRingsMAX())));
        wereables.appendChild(maxRings);

        Element items = doc.createElement("items");
        wereables.appendChild(items);

        Iterator<Item> iterator = wizard.getWearables().iterator();
        iterator.forEachRemaining(itemClass -> {
            Element item = doc.createElement("item");
            items.appendChild(item);

            Element type = doc.createElement("type");
            if (itemClass instanceof Weapon) {
                type.appendChild(doc.createTextNode("weapon"));
            } else if (itemClass instanceof Necklace) {
                type.appendChild(doc.createTextNode("necklace"));
            } else if (itemClass instanceof Ring) {
                type.appendChild(doc.createTextNode("ring"));
            }
            item.appendChild(type);

            Element element = doc.createElement("element");
            item.appendChild(element);
            element.appendChild(doc.createTextNode(itemClass.getDomain().toString()));

            Element value = doc.createElement("value");
            item.appendChild(value);
            value.appendChild(doc.createTextNode(String.valueOf(itemClass.getValue())));

        });

        Element crystalCarrier = doc.createElement("crystalCarrier");
        wizardElement.appendChild(crystalCarrier);

        Element capacity = doc.createElement("capacity");
        capacity.appendChild(doc.createTextNode(String.valueOf(wizard.getCrystalCarrier().getValue())));
        crystalCarrier.appendChild(capacity);

        Element crystals = doc.createElement("crystals");
        crystalCarrier.appendChild(crystals);

        Iterator<Item> itemCrystal = wizard.getCrystalCarrier().iterator();
        itemCrystal.forEachRemaining(item -> {
            Element crystal = doc.createElement("crystal");
            crystal.setAttribute("singa", String.valueOf(item.getValue()));
            crystals.appendChild(crystal);
        });

        Element jeweleryBag = doc.createElement("jeweleryBag");
        wizardElement.appendChild(jeweleryBag);

        Element capacityJW = doc.createElement("capacity");
        capacityJW.appendChild(doc.createTextNode(String.valueOf(wizard.getJewelryBag().getValue())));
        jeweleryBag.appendChild(capacityJW);


        Iterator<Item> iteratorJewlery = wizard.getJewelryBag().iterator();
        iteratorJewlery.forEachRemaining(itemClass -> {
            Element item = doc.createElement("item");
            items.appendChild(item);

            Element type = doc.createElement("type");
            if (itemClass instanceof Weapon) {
                type.appendChild(doc.createTextNode("weapon"));
            } else if (itemClass instanceof Necklace) {
                type.appendChild(doc.createTextNode("necklace"));
            } else if (itemClass instanceof Ring) {
                type.appendChild(doc.createTextNode("ring"));
            }
            item.appendChild(type);

            Element element = doc.createElement("element");
            item.appendChild(element);
            element.appendChild(doc.createTextNode(itemClass.getDomain().toString()));

            Element value = doc.createElement("value");
            item.appendChild(value);
            value.appendChild(doc.createTextNode(String.valueOf(itemClass.getValue())));

        }); */
    }

    private void guardarConditions(Element conditionsElement, Document doc, List<Condition> conditions) {
        for (Condition condition : conditions) {

            if (condition instanceof SimpleCondition) {
                //nothing
            } else if (condition instanceof FindCreatureCondition) {
                Element conditionElement = doc.createElement("condition");
                conditionElement.appendChild(doc.createTextNode("FindCreatureCondition"));
                conditionsElement.appendChild(conditionElement);
            } else if (condition instanceof FindObjectCondition) {
                Element conditionElement = doc.createElement("condition");
                conditionElement.appendChild(doc.createTextNode("FindObjectCondition"));
                conditionsElement.appendChild(conditionElement);
            } else if (condition instanceof KillAllCreaturesCondition) {
                Element conditionElement = doc.createElement("condition");
                conditionElement.appendChild(doc.createTextNode("KillAllCreaturesCondition"));
                conditionsElement.appendChild(conditionElement);
            } else if (condition instanceof VisitAllRoomsCondition) {
                Element conditionElement = doc.createElement("condition");
                conditionElement.appendChild(doc.createTextNode("VisitAllRoomsCondition"));
                conditionsElement.appendChild(conditionElement);
            }
        }
    }

    private void guardarDungeon(Element dungeonElement, Document doc, Dungeon dungeon) { /*
        Element rooms = doc.createElement("rooms");
        dungeonElement.appendChild(rooms);

        Iterator<Room> iterator = dungeon.iterator();
        iterator.forEachRemaining(itemClass -> {

            Element room = doc.createElement("room");
            rooms.appendChild(room);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(String.valueOf(itemClass.getID())));
            room.appendChild(id);

            Element exit = doc.createElement("exit");
            exit.appendChild(doc.createTextNode(String.valueOf(itemClass.isExit())));
            room.appendChild(exit);

            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(itemClass.getDescription()));
            room.appendChild(description);

            Element visited = doc.createElement("visited");
            visited.appendChild(doc.createTextNode(String.valueOf(itemClass.isVisited())));
            room.appendChild(visited);

            Element items = doc.createElement("items");
            room.appendChild(items);

            Iterator<Item> iteratorItems = itemClass.getContainer().iterator();
            iteratorItems.forEachRemaining(itemInRoom -> {
                Element item = doc.createElement("item");
                items.appendChild(item);

                Element type = doc.createElement("type");
                ;
                if (itemInRoom instanceof Weapon) {
                    type.appendChild(doc.createTextNode("weapon"));
                } else if (itemInRoom instanceof Necklace) {
                    type.appendChild(doc.createTextNode("necklace"));
                } else if (itemInRoom instanceof Ring) {
                    type.appendChild(doc.createTextNode("ring"));
                }
                item.appendChild(type);

                Element element = doc.createElement("element");
                item.appendChild(element);
                element.appendChild(doc.createTextNode(itemInRoom.getDomain().toString()));

                Element value = doc.createElement("value");
                item.appendChild(value);
                value.appendChild(doc.createTextNode(String.valueOf(itemInRoom.getValue())));

            });

            Element creature = doc.createElement("creature");
            room.appendChild(creature);
            if (itemClass.getCreature() != null) {
                Element element = doc.createElement("element");
                element.appendChild(doc.createTextNode(String.valueOf(itemClass.getCreature().getDomain().toString())));
                creature.appendChild(element);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(itemClass.getCreature().getName()));
                creature.appendChild(name);

                Element life = doc.createElement("life");
                life.appendChild(doc.createTextNode(String.valueOf(itemClass.getCreature().getLife())));
                creature.appendChild(life);

                Element attack = doc.createElement("attack");
                attack.appendChild(doc.createTextNode(String.valueOf(itemClass.getCreature().getRandomAttack().getDamage())));
                creature.appendChild(attack);

                Element spells = doc.createElement("spells");
                creature.appendChild(spells);

                Iterator<Spell> spellsIterator = itemClass.getCreature().getMemory().iterator();

                spellsIterator.forEachRemaining(spellClass -> {
                    Element spell = doc.createElement("spell");
                    spells.appendChild(spell);

                    Element element2 = doc.createElement("element");
                    spell.appendChild(element2);
                    element2.appendChild(doc.createTextNode(spellClass.getDomain().toString()));

                    Element level = doc.createElement("level");
                    spell.appendChild(level);
                    level.appendChild(doc.createTextNode(String.valueOf(spellClass.getLevel())));
                });
            }
        });*/
    }
}

