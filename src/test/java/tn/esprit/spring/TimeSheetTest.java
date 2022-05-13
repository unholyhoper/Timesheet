package tn.esprit.spring;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IEntrepriseService;
import tn.esprit.spring.services.ITimesheetService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TimeSheetTest {
    @Autowired
    IEntrepriseService entrepriseService;
    @Autowired
    ITimesheetService timesheetService;
    @Autowired
    IEmployeService employeService;
    @Autowired
    TimesheetRepository timesheetRepository;
    @Autowired
    MissionRepository missionRepository;

    @Test
    public void testAjouterDepartement() {
        Departement departement = new Departement("dept");
        entrepriseService.ajouterDepartement(departement);
        Assert.assertTrue("success",departement.getId()>0);
    }

    @Test
    public void testAffecterDepartementAEntreprise() {
        Entreprise entreprise = new Entreprise("testEntreprise", "logiciel");
        entrepriseService.ajouterEntreprise(entreprise);
        Departement departement = new Departement("electonic");
        entrepriseService.ajouterDepartement(departement);
        entrepriseService.affecterDepartementAEntreprise(departement.getId(), entreprise.getId());
        //load entreprise from database
        Entreprise loadedEntreprise = entrepriseService.getEntrepriseById(entreprise.getId());
        assertNotNull(loadedEntreprise.getDepartements());
    }

    @Test
    public void testTimeSheetAffecteAvecEmployeTechnicien() throws ParseException {
        Employe technicien = new Employe("nom de TECHNICIEN", "prenom de TECHNICIENT", "technicien@test1.com", true, Role.TECHNICIEN);
        //employé qui va etre affecter à une mission
        Employe employe = new Employe("nom employer a misssion", "prenom employer a mission", "employemission@test1.com", true, Role.INGENIEUR);
        Mission mission = new Mission("Mission de test", "description mission pour lancer ce test unitaire");
        Timesheet timesheet = new Timesheet();
        timesheet.setEmploye(technicien);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date dateDebut = formatter.parse("01-01-2022");
        Date dateFin = formatter.parse("01-06-2022");
        employeService.ajouterEmploye(employe);
        employeService.ajouterEmploye(technicien);
        timesheetService.ajouterMission(mission);
        timesheetService.ajouterTimesheet(mission.getId(), employe.getId(), dateDebut, dateFin);


        timesheetService.validerTimesheet(mission.getId(), technicien.getId(), dateDebut, dateFin, technicien.getId());

        Collection<Timesheet> timesheets = employeService.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);

        for (Timesheet timesheet1 : timesheets) {
            assertFalse(timesheet1.isValide());
        }
    }

    @Test
    public void testTimeSheetAvecEmployeDifferentDepartementQueLaMission() throws ParseException {
        Employe technicien = new Employe("nom de TECHNICIEN", "prenom de TECHNICIENT", "technicien@test.com", true, Role.TECHNICIEN);
        //employé qui va etre affecter à une mission
        Employe chefDepartmenet = new Employe("Nom CHEF DEPARTEMENT", "prenom Nom CHEF DEPARTEMENT", "chefdepartement@test.com", true, Role.CHEF_DEPARTEMENT);
        Mission mission = new Mission("Mission de test", "description mission pour lancer ce test unitaire");

        Departement departement1 = new Departement("Departement test 1");
        Departement departement2 = new Departement("Departement test 2");

        mission.setDepartement(departement1);
        chefDepartmenet.setDepartements(Arrays.asList(departement1));
        Timesheet timesheet = new Timesheet();
        timesheet.setEmploye(technicien);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date dateDebut = formatter.parse("01-01-2022");
        Date dateFin = formatter.parse("01-06-2022");
        entrepriseService.ajouterDepartement(departement1);
        entrepriseService.ajouterDepartement(departement2);
        employeService.ajouterEmploye(chefDepartmenet);
        employeService.ajouterEmploye(technicien);
        timesheetService.ajouterMission(mission);
        timesheetService.ajouterTimesheet(mission.getId(), technicien.getId(), dateDebut, dateFin);


        timesheetService.validerTimesheet(mission.getId(), technicien.getId(), dateDebut, dateFin, chefDepartmenet.getId());


        Collection<Timesheet> timesheets = employeService.getTimesheetsByMissionAndDate(technicien, mission, dateDebut, dateFin);

        for (Timesheet timesheet1 : timesheets) {
            assertFalse(timesheet1.isValide());
        }
    }

//
//    @Test
//    public void testTimeSheetSuccess() throws ParseException {
//        Employe technicien = new Employe("nom de TECHNICIEN", "prenom de TECHNICIENT", "technicien@test3.com", true, Role.TECHNICIEN);
//        //employé qui va etre affecter à une mission
//        Employe chefDepartmenet = new Employe("Nom CHEF DEPARTEMENT", "prenom Nom CHEF DEPARTEMENT", "chefdepartement@test3.com", true, Role.CHEF_DEPARTEMENT);
//        Mission mission = new Mission("Mission de test", "description mission pour lancer ce test unitaire");
//
//        Departement departement = new Departement("Departement test");
//        mission.setDepartement(departement);
//        chefDepartmenet.setDepartements(Arrays.asList(departement));
//        Timesheet timesheet = new Timesheet();
//        timesheet.setEmploye(technicien);
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//        Date dateDebut = formatter.parse("01-01-2022");
//        Date dateFin = formatter.parse("01-06-2022");
//        employeService.ajouterEmploye(chefDepartmenet);
//        employeService.ajouterEmploye(technicien);
//        entrepriseService.ajouterDepartement(departement);
//        timesheetService.ajouterMission(mission);
//        timesheetService.ajouterTimesheet(mission.getId(), technicien.getId(), dateDebut, dateFin);
//
//
//        timesheetService.validerTimesheet(mission.getId(), technicien.getId(), dateDebut, dateFin, chefDepartmenet.getId());
//
//
//        Collection<Timesheet> timesheets = employeService.getTimesheetsByMissionAndDate(technicien, mission, dateDebut, dateFin);
//
//        for (Timesheet timesheet1 : timesheets) {
//            assertFalse(timesheet1.isValide());
//        }
//    }
//
}
