package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndAddStudentTest {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";

	public static final String URL = "http://localhost:3000";
	
	public static final int TEST_STUDENT_ID = 9;
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";
	
	public static final int SLEEP_DURATION = 1000; // 1 second.
	
	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudent () throws Exception {
		
		Student x = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
		if (x != null) {
			studentRepository.delete(x);
		}
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		
		
		
		try {
			WebElement we;
			
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			we = driver.findElement(By.id("addStudent"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			//enter student name
			we = driver.findElement(By.name("name"));
			we.sendKeys(TEST_STUDENT_NAME);
			
			//enter student email
			we = driver.findElement(By.name("email"));
			we.sendKeys(TEST_STUDENT_EMAIL);
			
			// find and click the submit button
			we = driver.findElement(By.id("submit"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			String toastMessage = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
			
			System.out.println("Message of the Toast Message is: "+toastMessage);
			
			Assert.assertEquals(toastMessage, "Student successfully added");
			
			Thread.sleep(5000);
			
			driver.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
			
		} finally {
			driver.quit();
		}
	}
}
