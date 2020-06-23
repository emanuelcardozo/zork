package diccionario;

import java.util.HashMap;
import java.util.Map;

public enum Modificador {
	// ACCIONES
	
	ARTICULO("la", "las", "el", "los", "al"),
    PREPOSICION("a", "ante", "bajo", "con", "contra", "de", "desde", "en", "entre", "hacia", "hasta", "para", "por", "segun", "sin", "sobre", "tras");
	
	// FIN ACCIONES

    private String[] sinonimo;

    private static Map<String, Modificador> sinonimos = new HashMap<>();
    static {
        for (Modificador b : Modificador.values()) {
            for (String alias : b.sinonimo) {
            	sinonimos.put(alias, b);
            }
        }
    }

    private Modificador(String ... aliases) {
        this.sinonimo = aliases;
    }

    public static Modificador buscarSinonimo(String alias) {
    	Modificador b = sinonimos.get(alias.toLowerCase());
        return b;
    }
	
	
}
