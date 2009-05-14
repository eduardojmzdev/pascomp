/**
 * Paquete que recoge los archivos que constituyen la interfaz 
 * gráfica del compilador.
 */
package compilador;


/**
 * Clase que implementa un Frame que usamos de manera
 * auxiliar en el compilador del Grupo 6 de plg.
 */
public class Ventana extends javax.swing.JFrame {

	/**
	 * Indica la version de JFrame.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Es una referencia al compilador.
	 */
	private Compilador referencia;
	
	/**
	 * Es el botón Aceptar.
	 */
    private javax.swing.JButton jButtonAceptar;
    
    /**
     * Es el área de texto que se utiliza para mostrar
     * información. 
     */
    private javax.swing.JTextArea jTextAreaTexto;
    
    /**
     *  Contenedor que se usa para crear el interfaz
     */
    private javax.swing.JDesktopPane jDesktopPane1;
    
    /**
     * Un panel con barras de desplazamiento
	 * para el área de texto.
     */
    private javax.swing.JScrollPane jScrollPane1;
    
    /**
     * Constructor por defecto
     */
	public Ventana() {
        initComponents();
    }
    
	/**
	 * 
	 * @param c es el objeto de la clase Compilador 
	 * que llama al objeto de la clase Ventana.
	 * @param mensaje es el texto que hay que mostrar
	 * en el área de texto.
	 */
    public Ventana(Compilador c,String mensaje) {
        referencia = c;
    	initComponents();
        jTextAreaTexto.setText(mensaje);
        referencia.setEnabled(false);
    }
    
    /**
     * Método privado que inicializa todos los atributos
	 * del entorno gráfico de la clase Ventana.
     */
    private void initComponents() {
        jTextAreaTexto = new javax.swing.JTextArea();
        jButtonAceptar = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 102));
        jTextAreaTexto.setColumns(20);
        jTextAreaTexto.setRows(5);
        jScrollPane1.setViewportView(jTextAreaTexto);

        jScrollPane1.setBounds(2, 2, 296, 223);
        jDesktopPane1.add(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jButtonAceptar.setText("Aceptar");
        jButtonAceptar.setBounds(110,250,80,20);
        
        jButtonAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonAceptarMouseClicked(evt);
            }
        });
        jTextAreaTexto.setEditable(false);
        jDesktopPane1.add(jButtonAceptar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jDesktopPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jDesktopPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
        );
        setResizable(false);
    }

    /**
     * Método que elimina el objeto de la clase Ventana
     * y devuelve el control al objeto de la clase Compilador
     * que le invocó. 
     * @param evt Evento asociado a pulsar con el ratón 
     * o bien al darle espacio sobre el botón Aceptar.
     */
    private void jButtonAceptarMouseClicked(java.awt.event.MouseEvent evt) {
        referencia.setEnabled(true);
    	dispose();
    }
    
    /**
     * Método que coloca en las coordenadas x e y el
     * botón Aceptar.
     * @param x nueva coordena en el eje x del botón Aceptar.
     * @param y nueva coordena en el eje y del botón Aceptar.
     */
    public void reSizeBoton(int x,int y){
    	 jButtonAceptar.setBounds(x,y,80,20);
    }
    
    /**
     * Método que sirve para definir la posición de la ventana
     * asi como su anchura y altura.
	 * @param x nueva coordena en el eje x del Frame.
     * @param y nueva coordena en el eje y del Frame.
     * @param w nueva anchura del Frame.
     * @param h nueva altura del Frame.
     */
    public void reSizeScrollPane(int x,int y,int w,int h){
    	jScrollPane1.setBounds(x, y, w, h);
    }
}

