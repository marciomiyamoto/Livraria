package livraria.controle.web.test;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class LivroTest {
	
	WebDriver driver;

	@Before
	public void inicializa() {
		System.setProperty("webdriver.chrome.driver", "C:\\Livraria\\chromedriver_win32\\chromedriver.exe");
		this.driver = new ChromeDriver();
	}
	
	@Test
	public void cadastrarLivro() {
		driver.get("http://localhost:8888/livraria-web/livro/cadastrar.xhtml");
		
		WebElement titulo = driver.findElement(By.name("cadastroLivro:titulo"));
		titulo.sendKeys("");
		WebElement autor = driver.findElement(By.name("cadastroLivro:autor"));
		autor.sendKeys("");
		WebElement ano = driver.findElement(By.name("cadastroLivro:ano"));
		ano.sendKeys("");
		WebElement edicao = driver.findElement(By.name("cadastroLivro:edicao"));
		edicao.sendKeys("");
		WebElement numPaginas = driver.findElement(By.name("cadastroLivro:numPaginas"));
		numPaginas.sendKeys("");
		WebElement codBarras = driver.findElement(By.name("cadastroLivro:codBarras"));
		codBarras.sendKeys("");
		WebElement altura = driver.findElement(By.name("cadastroLivro:altura"));
		altura.sendKeys("");
		WebElement largura = driver.findElement(By.name("cadastroLivro:largura"));
		largura.sendKeys("");
		WebElement peso = driver.findElement(By.name("cadastroLivro:peso"));
		peso.sendKeys("");
		WebElement profundidade = driver.findElement(By.name("cadastroLivro:profundidade"));
		profundidade.sendKeys("");
		WebElement sinopse = driver.findElement(By.name("cadastroLivro:sinopse"));
		sinopse.sendKeys("");
		
		Select categoria = new Select(driver.findElement(By.name("cadastroLivro:selEditoras_input")));
		categoria.selectByVisibleText("Editora 2");
		Select grupoPrec = new Select(driver.findElement(By.name("cadastroLivro:selGrupoPrec_input")));
		grupoPrec.selectByVisibleText("Grupo 3");
		
		Select listCat = new Select(driver.findElement(By.name("cadastroLivro:pickCat_source")));
		listCat.selectByValue("Categoria 1");
		listCat.selectByValue("Categoria 2");
		Select listSubcat = new Select(driver.findElement(By.name("cadastroLivro:pickSubcat_source")));
		listSubcat.selectByValue("Sub 3");
		listSubcat.selectByValue("Sub 4");
		
		WebElement btnSalvar = driver.findElement(By.name("cadastroLivro:btnSalvar"));
		btnSalvar.click();
		
		boolean cadastroSucesso = driver.getPageSource().contains("Livro cadastrado com sucesso!");
		assertTrue(cadastroSucesso);
	}
	
	@After
	public void finaliza() {
		driver.close();
	}
}
