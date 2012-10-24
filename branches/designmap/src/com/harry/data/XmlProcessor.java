package com.harry.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlProcessor {
	
	public final static String USERS_PATH = "resources/data/XML/Users.xml";
	public final static String MAPS_PATH = "resources/data/XML/Maps.xml";
	public final static String COLLECTIONS_PATH = "resources/data/XML/Collections.xml";
	public final static int ID_VALUE = 0;
	public final static int NAME_VALUE = 1;
	public final static int PASSWORD_VALUE = 2;
	public final static int LEVEL_VALUE = 3;
	public final static int SCORE_VALUE = 4;
	public final static int MAP_PATH = 5;
	public final static int PICTURE_PATH = 6;
	public final static int USER_HIGH_SCORE = 7;
	public final static int HIGH_SCORE = 8;
	
	private static Document[] docs = new Document[3];
	
	public static void main(String[] args) {
	    //updateScoreUser("dung_harry", 500);
	    //createUser("cat", "stupid");
	    updateScoreUser("dung_harry", 1500);
	    HighScore[] test = getHighScore(5);
	    
	    System.out.println(test.length);
	    
	    for(int i = 0; i < test.length; i ++) {
	    	System.out.println((i + 1) +". Player name: " + test[i].getUserName() + ", score: " + test[i].getScore() + ".");
	    }
	}
	
	public XmlProcessor() {
		createFile();
	}
	
	public static void createFile() {
		File[] file = new File[3];
		file[0] = new File(USERS_PATH);
		file[1] = new File(MAPS_PATH);
		file[2] = new File(COLLECTIONS_PATH);
		
		docs[0] = new Document();
		docs[1] = new Document();
		docs[2] = new Document();
		
		if( !file[0].exists() ) {
		    docs[0].setRootElement(new Element("users"));
		    docs[0].getRootElement().setAttribute("numbers", "0");
		    
		    updateDocument(docs[0], USERS_PATH);
		}
		
		if( !file[1].exists() ) {
		    docs[1].setRootElement(new Element("maps"));
		    docs[1].getRootElement().setAttribute("numbers", "0");
		    
		    updateDocument(docs[1], MAPS_PATH);
	    }
		
		if( !file[2].exists() ) {
		    docs[2].setRootElement(new Element("collections"));
		    docs[2].getRootElement().setAttribute("numbers", "0");
		    
		    updateDocument(docs[2], COLLECTIONS_PATH);
		}
	}
	
	private static Document getDocument(String path) {
		SAXBuilder builder = new SAXBuilder();
		
		try {
			return builder.build(new File(path));
		} catch (JDOMException e) {
			e.printStackTrace();
			
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	private static void updateDocument(Document doc, String path) {
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		
		try {
			xmlOutput.output(doc, new FileOutputStream(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createUser(String name, String password) {
		Document usersDocument = getDocument(USERS_PATH);
		String sumUser;
		int temple = 0;
		Element[] childUser = new Element[4];
		Element rootElement = usersDocument.getRootElement();
		
		sumUser = rootElement.getAttributeValue("numbers");
		
		temple = Integer.parseInt(sumUser);
		
		Element newUser = new Element("user");
		newUser.setAttribute("id", Integer.toString(temple + 1));
		
	    childUser[0] = new Element("name");
	    childUser[0].addContent(name);
	    
	    childUser[1] = new Element("password");
	    childUser[1].addContent(password);
	    
	    childUser[2] = new Element("level");
	    childUser[2].addContent("0");
	    
	    childUser[3] = new Element("score");
	    childUser[3].addContent("0");
	    
	    for(int i = 0; i < childUser.length; i ++) {
	    	newUser.addContent(childUser[i]);
	    }
	    
	    rootElement.addContent(newUser);
	    
	    rootElement.setAttribute("numbers", Integer.toString(temple + 1));
	    
	    updateDocument(usersDocument, USERS_PATH);
	}
	
	public static void updateLevelUser(String userName, int level) {
		Document usersDocument = getDocument(USERS_PATH);
		Element rootElement = usersDocument.getRootElement();
		
		for( Element eachElement : rootElement.getChildren("user")) {
			if(eachElement.getChild("name").getValue().equals(userName)) {
				eachElement.getChild("level").setContent(new Text(Integer.toString(level)));
			}
		}
		
		updateDocument(usersDocument, USERS_PATH);
	}
	
	public static void updateScoreUser(String userName, int totalScore) {
		Document usersDocument = getDocument(USERS_PATH);
		Element rootElement = usersDocument.getRootElement();
		
		for( Element eachElement : rootElement.getChildren("user") ) {
			if(eachElement.getChild("name").getValue().equals(userName)) {
				eachElement.getChild("score").setContent(new Text(Integer.toString(totalScore)));
			}
		}
		
		updateDocument(usersDocument, USERS_PATH);
	}
	
	public static void updateScoreUser(String userName) {
		Document usersDocument = getDocument(USERS_PATH);
		Document collectionsDocument = getDocument(COLLECTIONS_PATH);
		int totalScore = 0;
		Element[] root =new Element[2];
		root[0] = usersDocument.getRootElement();
		root[1] = collectionsDocument.getRootElement();
		
		for( Element eachElement : root[1].getChildren("scores") ) {
			if(eachElement.getAttributeValue("username").equals(userName)) {
				for( Element value : eachElement.getChildren("value")) {
					totalScore += Integer.parseInt(value.getValue());
				}
			}
		}
		
		for( Element eachElement : root[0].getChildren("user") ) {
			if(eachElement.getChild("name").getValue().equals(userName)) {
				eachElement.getChild("score").setContent(new Text(Integer.toString(totalScore)));
			}
		}
		
		updateDocument(usersDocument, USERS_PATH);
	}
	
	public static String getInformationUser(String userName, int type) {
		String[] information = new String[5];
		Document usersDocument = getDocument(USERS_PATH);
	    Element rootElement = usersDocument.getRootElement();
	    
	    for( Element eachElement : rootElement.getChildren("user") ) {
	    	if(eachElement.getChildText("name").equals(userName)) {
	    		information[0] = eachElement.getAttributeValue("id");
	    		information[1] = eachElement.getChildText("name");
	    		information[2] = eachElement.getChildText("password");
	    		information[3] = eachElement.getChildText("level");
	    		information[4] = eachElement.getChildText("score");
	    	}
	    }
	    
	    for(int i = 0; i < information.length; i ++) {
	    	System.out.println(information[i]);
	    }
		
		if((type >= 0) && (type < information.length)) {
			return information[type];
		}
		else {
			return null;
		}
	}
	
	public static boolean checkSignIn(String userName, String password) {
		Document usersDocument = getDocument(USERS_PATH);
		Element rootElement = usersDocument.getRootElement();
		
		for( Element eachElement : rootElement.getChildren("user")) {
			if(eachElement.getChild("name").getValue().equals(userName) && eachElement.getChild("password").getValue().equals(password)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean checkCreateAccount(String newUserName) {
		Document usersDocument = getDocument(USERS_PATH);
		Element rootElement = usersDocument.getRootElement();
		
		for( Element eachElement : rootElement.getChildren("user")) {
			if(eachElement.getChild("name").getValue().equals(newUserName)) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void createMap(int level, String designer) {
		Document mapsDocument = getDocument(MAPS_PATH);
		Element rootElement = mapsDocument.getRootElement();
		Element[] mapChild = new Element[3]; 
		int sumMap = 0;
		int sumMaps = Integer.parseInt(rootElement.getAttributeValue("numbers"));
		Element elementLevel = null;
		
		for( Element eachElement : rootElement.getChildren("level") ) {
			if(eachElement.getAttributeValue("value").equals(Integer.toString(level))) {
				elementLevel = eachElement;
				sumMap = Integer.parseInt(eachElement.getAttributeValue("numbers"));
			}
		}
		
		if(elementLevel == null) {
		    elementLevel = new Element("level");
		    elementLevel.setAttribute("value", Integer.toString(level));
		    elementLevel.setAttribute("numbers", "0");
		    rootElement.addContent(elementLevel);
		    
		    sumMaps ++;
		    
		    rootElement.setAttribute("numbers", Integer.toString(sumMaps));
		}
		
		Element newMap = new Element("map");
		newMap.setAttribute("id", Integer.toString(sumMap + 1));
		
		mapChild[0] = new Element("designer");
		mapChild[0].addContent(new Text(designer));
		
		mapChild[1] = new Element("path");
		mapChild[1].addContent(new Text("resources/data/maps/level" + Integer.toString(level) + "/map" + Integer.toString(level) + "." + newMap.getAttributeValue("id") + ".txt"));
		
		mapChild[2] = new Element("picture");
		mapChild[2].addContent(new Text("resources/data/pictures/level" + Integer.toString(level) + "/picture" + Integer.toString(level) + "." + newMap.getAttributeValue("id") + ".png"));
		
		for(int i = 0; i < mapChild.length; i ++) {
			newMap.addContent(mapChild[i]);
		}
		
		elementLevel.setAttribute("numbers", Integer.toString(sumMap + 1));
		elementLevel.addContent(newMap);
		
		updateDocument(mapsDocument, MAPS_PATH);
	}
	
	public static void createCollection(String userName, int level, int score) {
		Document collectionsDocument = getDocument(COLLECTIONS_PATH);
		Element rootElement = collectionsDocument.getRootElement();
		int sumScores = Integer.parseInt(rootElement.getAttributeValue("numbers"));
		int sumLevel = 0;
		Element valueScore = null;
		Element newScores = null;
		
		for( Element eachElement :  rootElement.getChildren("scores")) {
			if(eachElement.getAttributeValue("username").equals(userName)) {
				newScores = eachElement;
			}
		}
		
		if(newScores == null) {
			newScores = new Element("scores");
			newScores.setAttribute("numbers", "0");
			newScores.setAttribute("username", userName);
			
			rootElement.addContent(newScores);
			rootElement.setAttribute("numbers", Integer.toString(sumScores + 1));
		}
		
		if(level > 0) {
			for( Element eachElement : newScores.getChildren("value") ) {
				if(eachElement.getAttributeValue("level").equals(Integer.toString(level))) {
					valueScore = eachElement;
				}
			}
			
			if(valueScore == null) {
			    sumLevel = Integer.parseInt(newScores.getAttributeValue("numbers"));
			    valueScore = new Element("value");
			    valueScore.setAttribute("level", Integer.toString(level));
			    valueScore.addContent(Integer.toString(score));
			
			    newScores.setAttribute("numbers", Integer.toString(sumLevel + 1));
			
			    newScores.addContent(valueScore);
			}
		}
		
		updateDocument(collectionsDocument, COLLECTIONS_PATH);
	}
	
	public static String getPath(int level, int type) {
		Document mapsDocument = getDocument(MAPS_PATH);
		Element rootElement = mapsDocument.getRootElement();
		int valueNext = 0;
		
		for(Element eachElement : rootElement.getChildren("level")) {
			if(eachElement.getAttributeValue("value").equals(Integer.toString(level))) {
				valueNext = Integer.parseInt(eachElement.getAttributeValue("numbers"));
				
				break;
			}
		}
		
		valueNext ++;
		
		if(type == XmlProcessor.MAP_PATH) {
		    return "resources/data/maps/level" + Integer.toString(level) + "/map" + Integer.toString(level) + "." + Integer.toString(valueNext) + ".txt";
	    }
		else if(type == XmlProcessor.PICTURE_PATH) {
			return "resources/data/pictures/level" + Integer.toString(level) + "/picture" + Integer.toString(level) + "." + Integer.toString(valueNext) + ".png";
		}
		else return "";
	}
	
	public static HighScore[] getHighScore(int number) {
		Document usersDocument = getDocument(USERS_PATH);
		Element rootElement = usersDocument.getRootElement();
		int size = Integer.parseInt(rootElement.getAttributeValue("numbers"));
		Element[] userElement = new Element[size];
	    Element templeElement = null;
	    HighScore[] highScore;
	    int index = 0;
	    
	    if(size <= number) {
	    	highScore = new HighScore[size];
	    }
	    else {
	    	highScore = new HighScore[number];
	    }
	    
	    for(int i = 0; i < highScore.length; i ++) {
	    	highScore[i] = new HighScore();
	    }
	    
	    for(Element eachElement : rootElement.getChildren("user")) {
	    	userElement[index] = eachElement;
	    	
	    	index ++;
	    }
	    
	    for(int i = 0; i < size; i ++) {
	    	templeElement = userElement[i];
	    	for(int j = i + 1; j < size; j ++) {
	    		if(Integer.parseInt(userElement[i].getChildText("score")) < Integer.parseInt(userElement[j].getChildText("score"))) {
	    			userElement[i] = userElement[j];
	    			userElement[j] = templeElement;
	    			templeElement = userElement[i];
	    		}
	    	}
	    }
	    
	    for(int i = 0; i < highScore.length; i ++) {
	    	highScore[i].setProperties(userElement[i]);
	    }
	    
	    return highScore;
	}
}