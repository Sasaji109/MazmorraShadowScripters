package load.loadImpl;

import game.Domain;
import game.character.Creature;
import game.character.Wizard;
import game.conditions.Condition;
import game.conditions.KillAllCreaturesCondition;
import game.conditions.VisitAllRoomsCondition;
import game.demiurge.Demiurge;
import game.dungeon.Dungeon;
import game.dungeon.Home;
import game.dungeon.Room;
import game.object.*;
import game.objectContainer.*;
import game.objectContainer.exceptions.ContainerFullException;
import game.objectContainer.exceptions.ContainerUnacceptedItemException;
import game.spell.*;
import game.spellContainer.Knowledge;
import load.LoadXml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoadXMLImpl implements LoadXml {
    public Demiurge load(String filePath) {
        try {
            File file = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            Element demiurgeElement = doc.getDocumentElement();
            Demiurge demiurge = new Demiurge();

            // Cargar datos generales de Demiurge
            int day = Integer.parseInt(demiurgeElement.getElementsByTagName("day").item(0).getTextContent());
            //demiurge.setDay(day);

            // Cargar datos de Home
            Element homeElement = (Element) demiurgeElement.getElementsByTagName("home").item(0);
            Home home = loadHome(homeElement);
            demiurge.setHome(home);

            // Cargar datos de Dungeon
            Element dungeonElement = (Element) demiurgeElement.getElementsByTagName("dungeon").item(0);
            Dungeon dungeon = loadDungeon(dungeonElement);
            demiurge.setDungeon(dungeon);

            // Cargar datos de Wizard
            Element wizardElement = (Element) demiurgeElement.getElementsByTagName("wizard").item(0);
            Wizard wizard = loadWizard(wizardElement);
            demiurge.setWizard(wizard);

            // Cargar condiciones
            Element conditionsElement = (Element) demiurgeElement.getElementsByTagName("conditions").item(0);
            List<Condition> conditions = loadConditions(conditionsElement, dungeon);
            conditions.forEach(demiurge::addCondition);

            return demiurge;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Home loadHome(Element homeElement) {
        String description = homeElement.getElementsByTagName("description").item(0).getTextContent();
        int comfort = Integer.parseInt(homeElement.getElementsByTagName("comfort").item(0).getTextContent());
        int singa = Integer.parseInt(homeElement.getElementsByTagName("singa").item(0).getTextContent());
        int singaCapacity = Integer.parseInt(homeElement.getElementsByTagName("singaCapacity").item(0).getTextContent());
        int singaMaxCapacity = Integer.parseInt(homeElement.getElementsByTagName("singaMaxCapacity").item(0).getTextContent());
        int chestValue = Integer.parseInt(homeElement.getElementsByTagName("chestValue").item(0).getTextContent());

        // Crear una instancia de Home con los datos cargados
        Knowledge knowledge = null;

        Home home = new Home(description, comfort, singa, singaMaxCapacity, new Chest(chestValue), knowledge  );

        // Cargar items en el chest
        NodeList items = homeElement.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element itemElement = (Element) items.item(i);
                String itemType = itemElement.getElementsByTagName("type").item(0).getTextContent();
                Domain itemDomain = Domain.valueOf(itemElement.getElementsByTagName("element").item(0).getTextContent());
                int itemValue = Integer.parseInt(itemElement.getElementsByTagName("value").item(0).getTextContent());

                Item item;
                switch (itemType) {
                    case "weapon":
                        item = new Weapon( itemValue);
                        break;
                    case "necklace":
                        item = new Necklace(itemDomain, itemValue);
                        break;
                    case "ring":
                        item = new Ring(itemDomain, itemValue);
                        break;
                    default:
                        // Handle unknown item type
                        continue;
                }

                try {
                    home.addItem(item);
                } catch (ContainerFullException | ContainerUnacceptedItemException e) {
                    // Handle exceptions
                    e.printStackTrace();
                }
            }

        }

        // Cargar spells en la library
        Element libraryElement = (Element) homeElement.getElementsByTagName("library").item(0);
        NodeList spells = libraryElement.getElementsByTagName("spell");
        for (int i = 0; i < spells.getLength(); i++) {
            if (spells.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element spellElement = (Element) spells.item(i);
                Domain spellDomain = Domain.valueOf(spellElement.getElementsByTagName("element").item(0).getTextContent());
                int spellLevel = Integer.parseInt(spellElement.getElementsByTagName("level").item(0).getTextContent());
                Spell spell=null;

                switch (spellDomain.name()){
                    case "AirAttack":
                        spell = new AirAttack();

                        break;
                    case "DrainEnergy":
                         spell = new DrainEnergy();

                        break;
                    case "ElectricAttack":
                        spell = new ElectricAttack();

                        break;
                    case "FireAttack":

                        spell = new FireAttack();
                        break;




                }

                home.getLibrary().add(spell);


            }
        }



        return home;
    }

    private Dungeon loadDungeon(Element dungeonElement) {
        Dungeon dungeon = new Dungeon();

        // Cargar datos generales de Dungeon si es necesario
        // ...

        // Cargar rooms
        Element roomsElement = (Element) dungeonElement.getElementsByTagName("rooms").item(0);
        NodeList roomNodes = roomsElement.getElementsByTagName("room");

        for (int i = 0; i < roomNodes.getLength(); i++) {
            if (roomNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element roomElement = (Element) roomNodes.item(i);

                int roomId = Integer.parseInt(roomElement.getElementsByTagName("id").item(0).getTextContent());
                boolean isExit = Boolean.parseBoolean(roomElement.getElementsByTagName("exit").item(0).getTextContent());
                String description = roomElement.getElementsByTagName("description").item(0).getTextContent();
                boolean isVisited = Boolean.parseBoolean(roomElement.getElementsByTagName("visited").item(0).getTextContent());

                // Crear una instancia de Room con los datos cargados
                //no sabemos si esto esta bien
                Room room = new Room(roomId, description, new RoomSet(roomId));
                //room.setExit(isExit);
                //room.setVisited(isVisited);

                // Cargar items en la room
                NodeList items = roomElement.getElementsByTagName("item");
                for (int j = 0; j < items.getLength(); j++) {
                    if (items.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element itemElement = (Element) items.item(j);
                        String itemType = itemElement.getElementsByTagName("type").item(0).getTextContent();
                        Domain itemDomain = Domain.valueOf(itemElement.getElementsByTagName("element").item(0).getTextContent());
                        int itemValue = Integer.parseInt(itemElement.getElementsByTagName("value").item(0).getTextContent());

                        Item item;
                        switch (itemType) {
                            case "weapon":
                                item = new Weapon(itemValue);
                                break;
                            case "necklace":
                                item = new Necklace(itemDomain, itemValue);
                                break;
                            case "ring":
                                item = new Ring(itemDomain, itemValue);
                                break;
                            default:
                                // Handle unknown item type
                                continue;
                        }

                        try {
                            room.addItem(item);
                        } catch (ContainerFullException | ContainerUnacceptedItemException e) {
                            // Handle exceptions
                            e.printStackTrace();
                        }
                    }
                }

                // Cargar datos de Creature en la room si existe
                Element creatureElement = (Element) roomElement.getElementsByTagName("creature").item(0);
                if (creatureElement != null) {
                    Creature creature = loadCreature(creatureElement);
                    room.setCreature(creature);
                }

                dungeon.addRoom(room);
            }
        }

        return dungeon;
    }

    private Creature loadCreature(Element creatureElement) {
        Domain creatureDomain = Domain.valueOf(creatureElement.getElementsByTagName("element").item(0).getTextContent());
        String creatureName = creatureElement.getElementsByTagName("name").item(0).getTextContent();
        int creatureLife = Integer.parseInt(creatureElement.getElementsByTagName("life").item(0).getTextContent());
        int creatureAttack = Integer.parseInt(creatureElement.getElementsByTagName("attack").item(0).getTextContent());

        Creature creature = new Creature(creatureName, creatureLife,creatureAttack,creatureDomain );


        // Cargar spells en la memory de la creature
        Element spellsElement = (Element) creatureElement.getElementsByTagName("spells").item(0);
        NodeList spellNodes = spellsElement.getElementsByTagName("spell");

        for (int i = 0; i < spellNodes.getLength(); i++) {
            if (spellNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element spellElement = (Element) spellNodes.item(i);
                Domain spellDomain = Domain.valueOf(spellElement.getElementsByTagName("element").item(0).getTextContent());
                int spellLevel = Integer.parseInt(spellElement.getElementsByTagName("level").item(0).getTextContent());
                Spell spell =null;
                switch (spellDomain.name()){
                    case "AirAttack":
                        spell = new AirAttack();

                        break;
                    case "DrainEnergy":
                        spell = new DrainEnergy();

                        break;
                    case "ElectricAttack":
                        spell = new ElectricAttack();

                        break;
                    case "FireAttack":

                        spell = new FireAttack();
                        break;




                }

                creature.getMemory().add(spell);
            }
        }

        return creature;
    }



    private Wizard loadWizard(Element wizardElement) {
        Wearables wearables = new Wearables(1,1,1);
        CrystalCarrier crystalCarrier = new CrystalCarrier(1);
        JewelryBag jewelryBag = new JewelryBag(1);
        Wizard wizard = new Wizard("2",1,1,1,1,wearables,crystalCarrier,jewelryBag);

        /*
        String wizardName = wizardElement.getElementsByTagName("name").item(0).getTextContent();
        int currentLife = Integer.parseInt(wizardElement.getElementsByTagName("currentLife").item(0).getTextContent());
        int maxLife = Integer.parseInt(wizardElement.getElementsByTagName("maxLife").item(0).getTextContent());
        int currentEnergy = Integer.parseInt(wizardElement.getElementsByTagName("currentEnergy").item(0).getTextContent());
        int maxEnergy = Integer.parseInt(wizardElement.getElementsByTagName("maxEnergy").item(0).getTextContent());


        // Cargar datos de wereables
        Element wereablesElement = (Element) wizardElement.getElementsByTagName("wereables").item(0);
        int maxWeapons = Integer.parseInt(wereablesElement.getElementsByTagName("maxWeapons").item(0).getTextContent());
        int maxNecklaces = Integer.parseInt(wereablesElement.getElementsByTagName("maxNecklaces").item(0).getTextContent());
        int maxRings = Integer.parseInt(wereablesElement.getElementsByTagName("maxRings").item(0).getTextContent());

        ///JewelryBag jewelryBag=new JewelryBag(maxNecklaces+maxRings);
        //CrystalCarrier crystalCarrier=new CrystalCarrier(0);


        Wearables wearables = new Wearables(maxWeapons, maxNecklaces, maxRings);

        // Cargar items en wereables
        NodeList items = wereablesElement.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element itemElement = (Element) items.item(i);
                String itemType = itemElement.getElementsByTagName("type").item(0).getTextContent();
                Domain itemDomain = Domain.valueOf(itemElement.getElementsByTagName("element").item(0).getTextContent());
                int itemValue = Integer.parseInt(itemElement.getElementsByTagName("value").item(0).getTextContent());

                Item item;
                switch (itemType) {
                    case "weapon":
                        item = new Weapon(itemValue);
                        break;
                    case "necklace":
                        item = new Necklace(itemDomain, itemValue);
                        break;
                    case "ring":
                        item = new Ring(itemDomain, itemValue);
                        break;
                    default:
                        // Handle unknown item type
                        continue;
                }

                try {
                    wearables.add(item);
                } catch (ContainerFullException | ContainerUnacceptedItemException e) {
                    // Handle exceptions
                    e.printStackTrace();
                }
            }
        }

        // Cargar datos del crystalCarrier
        Element crystalCarrierElement = (Element) wizardElement.getElementsByTagName("crystalCarrier").item(0);
        int crystalCarrierCapacity = Integer.parseInt(crystalCarrierElement.getElementsByTagName("capacity").item(0).getTextContent());
        CrystalCarrier crystalCarrier=new CrystalCarrier(crystalCarrierCapacity);

        // Cargar crystals en crystalCarrier
        NodeList crystals = crystalCarrierElement.getElementsByTagName("crystal");
        for (int i = 0; i < crystals.getLength(); i++) {
            if (crystals.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element crystalElement = (Element) crystals.item(i);
                int crystalSinga = Integer.parseInt(crystalElement.getAttribute("singa"));

                Item crystal = new SingaCrystal(crystalSinga);
                try {
                    crystalCarrier.add(crystal);
                } catch (ContainerUnacceptedItemException | ContainerFullException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Cargar datos del jeweleryBag
        Element jeweleryBagElement = (Element) wizardElement.getElementsByTagName("jeweleryBag").item(0);
        int jeweleryBagCapacity = Integer.parseInt(jeweleryBagElement.getElementsByTagName("capacity").item(0).getTextContent());
        JewelryBag jewelryBag=new JewelryBag(jeweleryBagCapacity);
        Wizard wizard = new Wizard(wizardName,currentLife, maxLife,currentEnergy, maxEnergy,wearables,crystalCarrier,jewelryBag);
        wizard.setLife(currentLife);
        wizard.setEnergy(currentEnergy);
        // Cargar items en jeweleryBag
        NodeList itemsJewelry = jeweleryBagElement.getElementsByTagName("item");
        for (int i = 0; i < itemsJewelry.getLength(); i++) {
            if (itemsJewelry.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemsJewelry.item(i);
                String itemType = itemElement.getElementsByTagName("type").item(0).getTextContent();
                Domain itemDomain = Domain.valueOf(itemElement.getElementsByTagName("element").item(0).getTextContent());
                int itemValue = Integer.parseInt(itemElement.getElementsByTagName("value").item(0).getTextContent());

                Item item;
                switch (itemType) {
                    case "weapon":
                        item = new Weapon(itemValue);
                        break;
                    case "necklace":
                        item = new Necklace(itemDomain, itemValue);
                        break;
                    case "ring":
                        item = new Ring(itemDomain, itemValue);
                        break;
                    default:
                        // Handle unknown item type
                        continue;
                }

                try {
                    wizard.getJewelryBag().add(item);
                } catch (ContainerFullException | ContainerUnacceptedItemException e) {
                    // Handle exceptions
                    e.printStackTrace();
                }
            }
        } */

        return wizard;
    }

    private List<Condition> loadConditions(Element conditionsElement, Dungeon dungeon) {
        List<Condition> conditions = new ArrayList<>();

        NodeList conditionNodes = conditionsElement.getChildNodes();
        for (int i = 0; i < conditionNodes.getLength(); i++) {
            if (conditionNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element conditionElement = (Element) conditionNodes.item(i);

                String conditionType = conditionElement.getTextContent();
                Condition condition;

                switch (conditionType) {
                    case "KillAllCreaturesCondition":
                        condition = new KillAllCreaturesCondition(dungeon);
                        break;
                    case "VisitAllRoomsCondition":
                        condition = new VisitAllRoomsCondition(dungeon);
                        break;
                    default:
                        // Handle unknown condition type
                        continue;
                }

                conditions.add(condition);
            }
        }

        return conditions;
    }

}

