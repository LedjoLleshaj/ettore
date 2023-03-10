package it.ettore.e2e.po.professor.lessons;

import it.ettore.e2e.po.Header;
import it.ettore.e2e.po.LessonDetailsPage;
import it.ettore.e2e.po.PageObject;
import lombok.EqualsAndHashCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class ProfessorLessonsPage extends PageObject {
    @FindBy(id = "btn-new-lesson")
    private WebElement addNewLesson;
    @FindBy(css = ".et-content > div")
    private List<WebElement> lessons;

    public ProfessorLessonsPage(WebDriver driver) {
        super(driver);
    }

    public Header headerComponent() {
        return new Header(driver);
    }

    public ProfessorLessonAddPage newLesson() {
        addNewLesson.click();
        return new ProfessorLessonAddPage(driver);
    }

    public List<LessonComponent> getLessons() {
        return lessons.stream().map(element -> new LessonComponent(driver, element)).collect(Collectors.toList());
    }

    @EqualsAndHashCode
    public static class LessonComponent {
        private final WebDriver driver;
        private final WebElement element;

        public LessonComponent(WebDriver driver, WebElement element) {
            this.driver = driver;
            this.element = element;
        }

        public String getTitle() {
            return element.findElement(By.className("et-name")).getText();
        }

        public String getDescription() {
            return element.findElement(By.className("et-description")).getText();
        }

        public LessonDetailsPage goTo() {
            element.findElement(By.className("et-name")).click();
            return new LessonDetailsPage(driver);
        }
    }
}
