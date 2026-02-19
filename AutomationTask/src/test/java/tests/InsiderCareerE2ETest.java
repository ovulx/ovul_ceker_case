package tests;

import base.BaseTest;
import base.DriverFactory;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LeverApplicationPage;
import pages.OpenPositionsPage;
import pages.QaCareersPage;

@org.testng.annotations.Listeners(utils.TestListener.class)
public class InsiderCareerE2ETest extends BaseTest {

    private OpenPositionsPage openPositions() {
        System.out.println("[STEP] Navigating to QA Careers page and opening Open Positions list (See all QA jobs).");
        var driver = DriverFactory.getDriver();
        return new QaCareersPage(driver)
                .open()
                .clickSeeAllQaJobs();
    }

    @Test
    public void requirement1_homePageShouldOpenAndMainBlocksLoaded() {
        System.out.println("[CASE 1] Start: Home page should open and main blocks should be loaded.");
        var driver = DriverFactory.getDriver();

        new HomePage(driver)
                .open()
                .assertOpenedAndMainBlocksLoaded();

        System.out.println("[CASE 1] End: Home page opened and main blocks are visible.");
        //org.testng.Assert.fail("screenshot test");  --Fail situation screenshot testing
    }

    @Test
    public void requirement2_filterQaJobsAndJobsListShouldBePresent() {
        System.out.println("[CASE 2] Start: Filter QA jobs by Location=Istanbul and Department=Quality Assurance, list should be present.");

        openPositions()
                .applyFiltersIstanbulAndQa()
                .assertJobsListPresent();

        System.out.println("[CASE 2] End: Filters applied and jobs list is present.");
    }

    @Test
    public void requirement3_allJobsShouldMatchPositionDepartmentLocationCriteria() {
        System.out.println("[CASE 3] Start: All jobs should match Position/Department/Location criteria.");

        openPositions()
                .applyFiltersIstanbulAndQa()
                .assertJobsListPresent()
                .assertAllJobsMatchCriteria();

        System.out.println("[CASE 3] End: All visible jobs match expected criteria.");
    }

    @Test
    public void requirement4_viewRoleShouldRedirectToLeverApplicationForm() {
        System.out.println("[CASE 4] Start: Clicking View Role should redirect to Lever application form in a new tab.");

        LeverApplicationPage lever = openPositions()
                .applyFiltersIstanbulAndQa()
                .assertJobsListPresent()
                .clickFirstCardViewRoleAndSwitchToNewTab();

        System.out.println("[STEP] Verifying redirection to Lever application page.");
        lever.assertRedirectedToLeverApplication();

        System.out.println("[CASE 4] End: Successfully redirected to Lever application page.");
    }
}
