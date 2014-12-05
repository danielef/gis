package us.cognicio.gis;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolygon;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

	public static final String API_KEY = "AIzaSyDOT_KUZQF_cOyMe7w4VMcmNxxezo4wXOE";
	
	public static final double DEFAULT_LAT = 19.4364413;
	public static final double DEFAULT_LON = -99.2122243;
	
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "us.cognicio.gis.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
   
    }

    @Override
    protected void init(VaadinRequest request) {
    	// Main Layout
        final VerticalLayout layout = new VerticalLayout();
        layout.addStyleName("v-gis");
        layout.setSizeFull();
        layout.setMargin(false);
        
        // Map Creation
        final GoogleMap map = new GoogleMap(API_KEY, null, null);
        map.setCenter(new LatLon(DEFAULT_LAT, DEFAULT_LON));
        map.setSizeFull();
        map.setZoom(17);
        
        // Add Markers
        addMarkers(map);
        
        // Add Poligons
        addPoligons(map);
        
        // Add Console
        addConsole(map);
                             
        // Add Map to Layout
        layout.addComponent(map);
        
        // Add Layout to HTML
        setContent(layout);
    }

    private void addMarkers(final GoogleMap map) {
    	// Marker
    	final GoogleMapMarker marker = new GoogleMapMarker("Torre Prisma",                       // Text
    													   new LatLon(DEFAULT_LAT, DEFAULT_LON), // Location
    													   false,                                // isDraggable
    													   null);                                // URL MarkerImage
    	
    	// Info
    	final GoogleMapInfoWindow info = new GoogleMapInfoWindow("<p>Torre Prisma</p><p>Av Horacio 1855, Polanco, \11550 Ciudad de México, D.F.</p><p>01 55 5557 5184</p>", 
    															 marker);
    	
    	// Marker Click
    	map.addMarkerClickListener(new MarkerClickListener(){
			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				map.openInfoWindow(info);
			}        	
        });
    	map.addMarker(marker);
    }
    
    private void addPoligons(final GoogleMap map) {
    	// Poligon vertex
    	final ArrayList<LatLon> points1 = new ArrayList<LatLon>(4);
    	points1.add(new LatLon(DEFAULT_LAT - 0.0000, DEFAULT_LON - 0.0005));
    	points1.add(new LatLon(DEFAULT_LAT - 0.0000, DEFAULT_LON + 0.0005));
    	points1.add(new LatLon(DEFAULT_LAT - 0.0005, DEFAULT_LON + 0.0005));
    	points1.add(new LatLon(DEFAULT_LAT - 0.0005, DEFAULT_LON - 0.0005));
    	
    	// Poligon
    	final GoogleMapPolygon polygon = new GoogleMapPolygon(points1, // Vertex
    														"#00aa00", // Fill Color
    														 0.8,      // Opacity 0.0 - 1.0
    														 "#000000",// Border Color
    														 0.5,      // Opacity 0.0 - 1.0
    														 2 );      // Border Stroke
        map.addPolygonOverlay(polygon);
    }
    
    
    private void addConsole(GoogleMap map) {
    	final Window console = new Window("eGIS Demo");
    	final HorizontalLayout layout = new HorizontalLayout();
    	final TextField latField = new TextField("Latitud");
    	final TextField lonField= new TextField("Longitud");
    	
    	// Initial Values
    	latField.setValue(DEFAULT_LAT+"");
		lonField.setValue(DEFAULT_LON+"");
    	
		// Update Coordinates
    	map.addMapMoveListener(new MapMoveListener() {
			@Override
			public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE, LatLon boundsSW) {
				latField.setValue(center.getLat()+"");
				lonField.setValue(center.getLon()+"");
			}    		
    	});
    	
    	layout.setMargin(true);
    	layout.addComponent(latField);
    	layout.addComponent(new Label("<div style='width:10px; height: 10px;'></div>", ContentMode.HTML));
    	layout.addComponent(lonField);
    	
    	console.setContent(layout);
    	console.setClosable(false);
    	console.setPositionX(80);
    	console.setPositionY(20);
    	addWindow(console);
    }
}
