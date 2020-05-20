package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Location extends Noun {
	private String description;
	private Map<String, Place> placesMap = new HashMap<String, Place>();
	private Map<String, NPC> npcsMap = new HashMap<String, NPC>();
	private Map<String, Connection> connectionByDirectionMap = new HashMap<String, Connection>();
	private Map<String, Connection> connectionByLocationMap = new HashMap<String, Connection>();

	private ArrayList<Connection> connectionsArray = new ArrayList<Connection>();

	public Location(String name, String gender, String number, String description, HashMap<String, Place> places,
			HashMap<String, NPC> npcs, ArrayList<Connection> connections) {
		super(name, gender, number);

		this.description = description;
		this.placesMap = places;
		this.npcsMap = npcs;
		this.connectionsArray = connections;
	}

	public Location(JSONObject locationJSON, Map<String, Item> itemsMap, Map<String, NPC> npcsMap) {
		super((String) locationJSON.get("name"), (String) locationJSON.get("gender"),
				(String) locationJSON.get("number"));
		this.description = (String) locationJSON.get("description");

		if (locationJSON.containsKey("places"))
			buildPlaces((JSONArray) locationJSON.get("places"), itemsMap);

		if (locationJSON.containsKey("npcs"))
			buildNPCS((JSONArray) locationJSON.get("npcs"), npcsMap);

		if (locationJSON.containsKey("connections"))
			buildConnections((JSONArray) locationJSON.get("connections"));

	}

	private void buildPlaces(JSONArray placesJSON, Map<String, Item> itemsMap) {
		for (Object placeObj : placesJSON) {
			JSONObject place = (JSONObject) placeObj;
			String placeName = (String) place.get("name");
			this.placesMap.put(placeName, new Place(place, itemsMap));
		}
	}

	private void buildNPCS(JSONArray npcsJSON, Map<String, NPC> npcsMap) {
		for (Object npcObj : npcsJSON) {
			String npcName = (String) npcObj;
			this.npcsMap.put(npcName, npcsMap.get(npcName));
		}
	}

	private void buildConnections(JSONArray connectionsJSON) {
		Connection connection;
		for (Object connectionObj : connectionsJSON) {
			JSONObject connectionJSON = (JSONObject) connectionObj;
//			connectionsArray.add(new Connection(connectionJSON));
			connection = new Connection(connectionJSON);
			connectionByDirectionMap.put(connection.getDirection(), connection);
			connectionByLocationMap.put(connection.getLocation(), connection);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Place> getPlacesMap() {
		return placesMap;
	}

	public void setPlacesMap(Map<String, Place> placesMap) {
		this.placesMap = placesMap;
	}

	public Map<String, NPC> getNpcsMap() {
		return npcsMap;
	}

	public void setNpcsMap(Map<String, NPC> npcsMap) {
		this.npcsMap = npcsMap;
	}

	public ArrayList<Connection> getConnectionsArray() {
		return connectionsArray;
	}

	public void setConnectionsArray(ArrayList<Connection> connectionsArray) {
		this.connectionsArray = connectionsArray;
	}

	@Override
	public String toString() {
		return getCantidad() + " " + getName();
	}

	public String describirLugaresConItems() {
		String descripcion = "";
		Place sitio;

		for (Map.Entry<String, Place> entry : placesMap.entrySet()) {
			sitio = entry.getValue();
			descripcion += "En " + sitio.getArticulo() + " " + sitio.getName() + " hay ";
			descripcion += sitio.listarItems();
		}

		return descripcion;
	}

	public String describirNPCs() {
		String[] palabras = new String[npcsMap.keySet().size()];
		NPC npc;
		int index = 0;

		for (Map.Entry<String, NPC> entry : npcsMap.entrySet()) {
			npc = entry.getValue();
			palabras[index++] = npc.getCantidad() + " " + npc.getName();
		}

		return "Hay " + listarPalabras(palabras);
	}

	public String describirConnections() {
		String[] palabras = new String[connectionsArray.size()];
		int index = 0;

		for (Connection connection : connectionsArray) {
			palabras[index++] = "Al " + connection.getDirection() + " se puede ir hacia " + connection.getLocation();
		}

		return listarPalabras(palabras);
	}

	public String moverHacia(String where) {

		Connection connection = connectionByDirectionMap.get(where);

		if (connection == null) {
			connection = connectionByLocationMap.get(where);
		}

		return connection.getLocation();
	}

	public boolean sePuedeMoverHacia(String where) {

		Connection connection = connectionByDirectionMap.get(where);

		if (connection == null) {
			connection = connectionByLocationMap.get(where);
		}

		return connection != null && connection.getObstacle() == null;
	}

	public String porqueNoPuedoIrHacia(String where) {

		Connection connection = connectionByDirectionMap.get(where);
		NPC obstaculo;

		if (connection == null) {
			connection = connectionByLocationMap.get(where);
		}

		if (connection == null)
			return "No existe esa ubicacion.";

		obstaculo = npcsMap.get(connection.getObstacle());

		return "'¡No puedes pasar!' " + obstaculo.getArticulo() + " " + obstaculo.getName() + " no te dejará pasar";
	}

}