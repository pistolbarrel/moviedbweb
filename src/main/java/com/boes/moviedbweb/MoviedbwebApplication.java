package com.boes.moviedbweb;

import com.boes.moviedbweb.entity.Movie;
import com.boes.moviedbweb.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoviedbwebApplication /*implements CommandLineRunner*/ {

//	@Autowired
//	private MovieService movieService;

	public static void main(String[] args) {
		SpringApplication.run(MoviedbwebApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		Movie movie = movieService.addDescription("Rendez-vous", "1885", "RENDEZ-VOUS (1985) won director André " +
//				"Téchiné the Best Director award at the Cannes Film Festival");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "Christopher Miles");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Glenda Jackson; Susannah York; Vivien Merchant;Berry White");
//
//		movie = movieService.addDescription("Starring Glenda Jackson Teaser", "", "One of the most lauded performers of her generation, Glenda Jackson is known both for her dazzling work on stage and screen and, in later years, her commanding career as an outspoken member of the UK Parliament’s ");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson");
//
//		movie = movieService.addDescription("Sunday Bloody Sunday", "1971", "John Schlesinger followed his iconic MIDNIGHT COWBOY with this deeply personal take on love and sex. SUNDAY BLOODY SUNDAY depicts the romantic lives of two");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "John Schlesinger \n");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Glenda Jackson; Peter Finch; Murray Head");
//
//		movie = movieService.addDescription("Music Lovers, The", "1971", "Ken Russell’s provocative biopic of famed composer Pyotr Ilyich Tchaikovsky (Richard Chamberlain) is a typically mad, perverse, deliriously over-the-top");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "Ken Russell\n");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Richard Chamberlain; Glenda Jackson; Max Adrian\n");
//
//		movie = movieService.addDescription("Maids, The", "1975", "Jean Genet receives an unbridled, expertly cinematic rendering in this adaptation of his perverse play. Glenda Jackson and Susannah York play Solange and");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "Christopher Miles \n");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Glenda Jackson; Susannah York; Vivien Merchant\n");
//
//		movie = movieService.addDescription("Stevie", "1978", "The great Glenda Jackson delivers one of her finest performances as celebrated British poet Stevie Smith (of “Not Waving but Drowning” fame) in this");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "Robert Enders \n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Glenda Jackson; Mona Washbourne; Alec McCowen\n");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//
//		movie = movieService.addDescription("Hopscotch", "1980", "The inimitable comic team of Walter Matthau and Glenda Jackson star in this nimble tale of international intrigue from master British filmmaker Ronald Neame.");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "Ronald Neame \n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Walter Matthau; Glenda Jackson; Sam Waterston\n");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//
//		movie = movieService.addDescription("Return of the Soldier, The", "1982", "In this magnificently rich, exquisitely acted adaptation of the 1918 novel by Rebecca West, wealthy Englishman Chris Baldry (Alan Bates) returns home from");
//		movieService.addDirectors(movie.getTitle(), movie.getYear(), "Alan Bridges \n");
//		movieService.addActors(movie.getTitle(), movie.getYear(), "Julie Christie; Glenda Jackson; Ann-Margret\n");
//		movieService.addCollections(movie.getTitle(), movie.getYear(), "Criterion:Starring Glenda Jackson\n");
//
//
//
//
//
//		for (Movie movieToPrint : movieService.lookup()) {
//			System.out.println("movie = " + movieToPrint);
//		}
//	}
}
