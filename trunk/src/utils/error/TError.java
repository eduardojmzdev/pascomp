/**
 * Paquete que recoge los recursos en los que se apoyan los distintos módulos
 * del compilador.
 */
package utils.error;

/**
 * Tipo enumerado compuesto por:
 * LEXICO: Error léxico.
 * SINTACTICO: Error sintáctico.
 * SEMANTICO: Error semántico.
 * EJECUCIÓN: Error en tiempo de ejecución.
 */
public enum TError {
	LEXICO,
	SINTACTICO,
	SEMANTICO,
	EJECUCION
}
