package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demo.model.ELocation;
import com.example.demo.model.Location;
import com.example.demo.repository.LocationRepository;

@Component
@Order(1) // Run before DataSeeder
public class LocationDataSeeder implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void run(String... args) throws Exception {
        if (locationRepository.count() == 0) {
            System.out.println("🗺️  Seeding Rwanda location data...");

            // Province: Kigali City
            createLocation("KGL", "Kigali City", null, ELocation.PROVINCE);

            // Districts in Kigali City
            createLocation("KGSP", "Gasabo", "KGL", ELocation.DISTRICT);
            createLocation("KGKL", "Kicukiro", "KGL", ELocation.DISTRICT);
            createLocation("KGNY", "Nyarugenge", "KGL", ELocation.DISTRICT);

            // Sectors in Gasabo District
            createLocation("KGSP01", "Bumbogo", "KGSP", ELocation.SECTOR);
            createLocation("KGSP02", "Gatsata", "KGSP", ELocation.SECTOR);
            createLocation("KGSP03", "Jali", "KGSP", ELocation.SECTOR);
            createLocation("KGSP04", "Gikomero", "KGSP", ELocation.SECTOR);
            createLocation("KGSP05", "Gisozi", "KGSP", ELocation.SECTOR);
            createLocation("KGSP06", "Jabana", "KGSP", ELocation.SECTOR);
            createLocation("KGSP07", "Kinyinya", "KGSP", ELocation.SECTOR);
            createLocation("KGSP08", "Ndera", "KGSP", ELocation.SECTOR);
            createLocation("KGSP09", "Nduba", "KGSP", ELocation.SECTOR);
            createLocation("KGSP10", "Remera", "KGSP", ELocation.SECTOR);
            createLocation("KGSP11", "Rusororo", "KGSP", ELocation.SECTOR);
            createLocation("KGSP12", "Rutunga", "KGSP", ELocation.SECTOR);

            // Cells in Remera Sector
            createLocation("KGSP10C1", "Rukiri I", "KGSP10", ELocation.CELL);
            createLocation("KGSP10C2", "Rukiri II", "KGSP10", ELocation.CELL);
            createLocation("KGSP10C3", "Nyarutarama", "KGSP10", ELocation.CELL);
            createLocation("KGSP10C4", "Kabuga", "KGSP10", ELocation.CELL);
            createLocation("KGSP10C5", "Remera", "KGSP10", ELocation.CELL);
            createLocation("KGSP10C6", "Gishushu", "KGSP10", ELocation.CELL);
            
            // Cells in Bumbogo Sector
            createLocation("KGSP01C1", "Bumbogo", "KGSP01", ELocation.CELL);
            createLocation("KGSP01C2", "Rwampara", "KGSP01", ELocation.CELL);
            createLocation("KGSP01C3", "Kinyinya", "KGSP01", ELocation.CELL);
            
            // Cells in Gatsata Sector
            createLocation("KGSP02C1", "Gatsata", "KGSP02", ELocation.CELL);
            createLocation("KGSP02C2", "Kimisagara", "KGSP02", ELocation.CELL);
            createLocation("KGSP02C3", "Nyabugogo", "KGSP02", ELocation.CELL);
            
            // Cells in Jali Sector
            createLocation("KGSP03C1", "Jali", "KGSP03", ELocation.CELL);
            createLocation("KGSP03C2", "Kacyiru", "KGSP03", ELocation.CELL);
            
            // Cells in Gikomero Sector
            createLocation("KGSP04C1", "Gikomero", "KGSP04", ELocation.CELL);
            createLocation("KGSP04C2", "Kimihurura", "KGSP04", ELocation.CELL);
            
            // Cells in Gisozi Sector
            createLocation("KGSP05C1", "Gisozi", "KGSP05", ELocation.CELL);
            createLocation("KGSP05C2", "Kacyiru", "KGSP05", ELocation.CELL);
            
            // Cells in Jabana Sector
            createLocation("KGSP06C1", "Jabana", "KGSP06", ELocation.CELL);
            createLocation("KGSP06C2", "Rusororo", "KGSP06", ELocation.CELL);
            
            // Cells in Kinyinya Sector
            createLocation("KGSP07C1", "Kinyinya", "KGSP07", ELocation.CELL);
            createLocation("KGSP07C2", "Nyagatovu", "KGSP07", ELocation.CELL);
            
            // Cells in Ndera Sector
            createLocation("KGSP08C1", "Ndera", "KGSP08", ELocation.CELL);
            createLocation("KGSP08C2", "Kinyinya", "KGSP08", ELocation.CELL);
            
            // Cells in Nduba Sector
            createLocation("KGSP09C1", "Nduba", "KGSP09", ELocation.CELL);
            createLocation("KGSP09C2", "Rutunga", "KGSP09", ELocation.CELL);
            
            // Cells in Rusororo Sector
            createLocation("KGSP11C1", "Rusororo", "KGSP11", ELocation.CELL);
            createLocation("KGSP11C2", "Nyagatovu", "KGSP11", ELocation.CELL);
            
            // Cells in Rutunga Sector
            createLocation("KGSP12C1", "Rutunga", "KGSP12", ELocation.CELL);
            createLocation("KGSP12C2", "Ndera", "KGSP12", ELocation.CELL);

            // Villages in Rukiri I Cell (sample)
            createLocation("KGSP10C1V1", "Agatare", "KGSP10C1", ELocation.VILLAGE);
            createLocation("KGSP10C1V2", "Kabeza", "KGSP10C1", ELocation.VILLAGE);
            createLocation("KGSP10C1V3", "Karuganda", "KGSP10C1", ELocation.VILLAGE);

            // Sectors in Kicukiro District
            createLocation("KGKL01", "Gahanga", "KGKL", ELocation.SECTOR);
            createLocation("KGKL02", "Gatenga", "KGKL", ELocation.SECTOR);
            createLocation("KGKL03", "Gikondo", "KGKL", ELocation.SECTOR);
            createLocation("KGKL04", "Kagarama", "KGKL", ELocation.SECTOR);
            createLocation("KGKL05", "Kanombe", "KGKL", ELocation.SECTOR);
            createLocation("KGKL06", "Kicukiro", "KGKL", ELocation.SECTOR);
            createLocation("KGKL07", "Kigarama", "KGKL", ELocation.SECTOR);
            createLocation("KGKL08", "Masaka", "KGKL", ELocation.SECTOR);
            createLocation("KGKL09", "Niboye", "KGKL", ELocation.SECTOR);
            createLocation("KGKL10", "Nyarugunga", "KGKL", ELocation.SECTOR);

            // Cells in Gikondo Sector
            createLocation("KGKL03C1", "Nyenyeri", "KGKL03", ELocation.CELL);
            createLocation("KGKL03C2", "Umubano", "KGKL03", ELocation.CELL);
            createLocation("KGKL03C3", "Gikondo", "KGKL03", ELocation.CELL);
            
            // Cells in Gahanga Sector
            createLocation("KGKL01C1", "Gahanga", "KGKL01", ELocation.CELL);
            createLocation("KGKL01C2", "Kanombe", "KGKL01", ELocation.CELL);
            
            // Cells in Gatenga Sector
            createLocation("KGKL02C1", "Gatenga", "KGKL02", ELocation.CELL);
            createLocation("KGKL02C2", "Kicukiro", "KGKL02", ELocation.CELL);
            
            // Cells in Kagarama Sector
            createLocation("KGKL04C1", "Kagarama", "KGKL04", ELocation.CELL);
            createLocation("KGKL04C2", "Niboye", "KGKL04", ELocation.CELL);
            
            // Cells in Kanombe Sector
            createLocation("KGKL05C1", "Kanombe", "KGKL05", ELocation.CELL);
            createLocation("KGKL05C2", "Kicukiro", "KGKL05", ELocation.CELL);
            
            // Cells in Kicukiro Sector
            createLocation("KGKL06C1", "Kicukiro", "KGKL06", ELocation.CELL);
            createLocation("KGKL06C2", "Nyarugunga", "KGKL06", ELocation.CELL);
            
            // Cells in Kigarama Sector
            createLocation("KGKL07C1", "Kigarama", "KGKL07", ELocation.CELL);
            createLocation("KGKL07C2", "Masaka", "KGKL07", ELocation.CELL);
            
            // Cells in Masaka Sector
            createLocation("KGKL08C1", "Masaka", "KGKL08", ELocation.CELL);
            createLocation("KGKL08C2", "Niboye", "KGKL08", ELocation.CELL);
            
            // Cells in Niboye Sector
            createLocation("KGKL09C1", "Niboye", "KGKL09", ELocation.CELL);
            createLocation("KGKL09C2", "Nyarugunga", "KGKL09", ELocation.CELL);
            
            // Cells in Nyarugunga Sector
            createLocation("KGKL10C1", "Nyarugunga", "KGKL10", ELocation.CELL);
            createLocation("KGKL10C2", "Gikondo", "KGKL10", ELocation.CELL);

            // Villages in Gikondo Cell (sample)
            createLocation("KGKL03C3V1", "Akarugezi", "KGKL03C3", ELocation.VILLAGE);
            createLocation("KGKL03C3V2", "Akayenzi", "KGKL03C3", ELocation.VILLAGE);
            createLocation("KGKL03C3V3", "Nyagatovu", "KGKL03C3", ELocation.VILLAGE);

            // Sectors in Nyarugenge District
            createLocation("KGNY01", "Gitega", "KGNY", ELocation.SECTOR);
            createLocation("KGNY02", "Kanyinya", "KGNY", ELocation.SECTOR);
            createLocation("KGNY03", "Kigali", "KGNY", ELocation.SECTOR);
            createLocation("KGNY04", "Kimisagara", "KGNY", ELocation.SECTOR);
            createLocation("KGNY05", "Mageragere", "KGNY", ELocation.SECTOR);
            createLocation("KGNY06", "Muhima", "KGNY", ELocation.SECTOR);
            createLocation("KGNY07", "Nyakabanda", "KGNY", ELocation.SECTOR);
            createLocation("KGNY08", "Nyamirambo", "KGNY", ELocation.SECTOR);
            createLocation("KGNY09", "Nyarugenge", "KGNY", ELocation.SECTOR);
            createLocation("KGNY10", "Rwezamenyo", "KGNY", ELocation.SECTOR);

            // Cells in Nyamirambo Sector
            createLocation("KGNY08C1", "Amahoro", "KGNY08", ELocation.CELL);
            createLocation("KGNY08C2", "Nyamirambo", "KGNY08", ELocation.CELL);
            createLocation("KGNY08C3", "Rugenge", "KGNY08", ELocation.CELL);
            
            // Cells in Gitega Sector
            createLocation("KGNY01C1", "Gitega", "KGNY01", ELocation.CELL);
            createLocation("KGNY01C2", "Kigali", "KGNY01", ELocation.CELL);
            
            // Cells in Kanyinya Sector
            createLocation("KGNY02C1", "Kanyinya", "KGNY02", ELocation.CELL);
            createLocation("KGNY02C2", "Kimisagara", "KGNY02", ELocation.CELL);
            
            // Cells in Kigali Sector
            createLocation("KGNY03C1", "Kigali", "KGNY03", ELocation.CELL);
            createLocation("KGNY03C2", "Nyakabanda", "KGNY03", ELocation.CELL);
            
            // Cells in Kimisagara Sector
            createLocation("KGNY04C1", "Kimisagara", "KGNY04", ELocation.CELL);
            createLocation("KGNY04C2", "Muhima", "KGNY04", ELocation.CELL);
            
            // Cells in Mageragere Sector
            createLocation("KGNY05C1", "Mageragere", "KGNY05", ELocation.CELL);
            createLocation("KGNY05C2", "Nyamirambo", "KGNY05", ELocation.CELL);
            
            // Cells in Muhima Sector
            createLocation("KGNY06C1", "Muhima", "KGNY06", ELocation.CELL);
            createLocation("KGNY06C2", "Nyakabanda", "KGNY06", ELocation.CELL);
            
            // Cells in Nyakabanda Sector
            createLocation("KGNY07C1", "Nyakabanda", "KGNY07", ELocation.CELL);
            createLocation("KGNY07C2", "Nyarugenge", "KGNY07", ELocation.CELL);
            
            // Cells in Nyarugenge Sector
            createLocation("KGNY09C1", "Nyarugenge", "KGNY09", ELocation.CELL);
            createLocation("KGNY09C2", "Rwezamenyo", "KGNY09", ELocation.CELL);
            
            // Cells in Rwezamenyo Sector
            createLocation("KGNY10C1", "Rwezamenyo", "KGNY10", ELocation.CELL);
            createLocation("KGNY10C2", "Gitega", "KGNY10", ELocation.CELL);

            // Villages in Nyamirambo Cell (sample)
            createLocation("KGNY08C2V1", "Quartier Matheus", "KGNY08C2", ELocation.VILLAGE);
            createLocation("KGNY08C2V2", "Quartier Nyamirambo", "KGNY08C2", ELocation.VILLAGE);
            createLocation("KGNY08C2V3", "Biryogo", "KGNY08C2", ELocation.VILLAGE);

            // Province: Southern Province
            createLocation("SP", "Southern Province", null, ELocation.PROVINCE);

            // Districts in Southern Province
            createLocation("SPHU", "Huye", "SP", ELocation.DISTRICT);
            createLocation("SPMU", "Muhanga", "SP", ELocation.DISTRICT);

            // Sectors in Huye District (sample)
            createLocation("SPHU01", "Huye", "SPHU", ELocation.SECTOR);
            createLocation("SPHU02", "Tumba", "SPHU", ELocation.SECTOR);
            createLocation("SPHU03", "Ngoma", "SPHU", ELocation.SECTOR);

            // Province: Western Province
            createLocation("WP", "Western Province", null, ELocation.PROVINCE);

            // Districts in Western Province
            createLocation("WPRU", "Rubavu", "WP", ELocation.DISTRICT);
            createLocation("WPRU01", "Rubavu", "WPRU", ELocation.SECTOR);
            createLocation("WPRU02", "Gisenyi", "WPRU", ELocation.SECTOR);

            // Province: Eastern Province
            createLocation("EP", "Eastern Province", null, ELocation.PROVINCE);

            // Districts in Eastern Province
            createLocation("EPRU", "Rwamagana", "EP", ELocation.DISTRICT);
            createLocation("EPRU01", "Rwamagana", "EPRU", ELocation.SECTOR);

            // Province: Northern Province
            createLocation("NP", "Northern Province", null, ELocation.PROVINCE);

            // Districts in Northern Province
            createLocation("NPGA", "Gicumbi", "NP", ELocation.DISTRICT);
            createLocation("NPGA01", "Gicumbi", "NPGA", ELocation.SECTOR);

            System.out.println("✅ Rwanda location data seeded successfully!");
            System.out.println("📍 Total locations created: " + locationRepository.count());
        } else {
            System.out.println("ℹ️  Location data already exists, skipping seeding.");
        }
    }

    private void createLocation(String code, String name, String parentCode, ELocation type) {
        Location location = new Location();
        location.setCode(code);
        location.setName(name);
        location.setParentCode(parentCode);
        location.setType(type);
        locationRepository.save(location);
    }
}
