package com.mangomusic.ui;

import com.mangomusic.data.ReportsDao;
import com.mangomusic.models.ReportResult;
import com.mangomusic.util.ConsoleColors;
import com.mangomusic.util.InputValidator;

import java.util.List;

public class SpecialReportsScreen {

    private final ReportsDao reportsDao;

    public SpecialReportsScreen(ReportsDao reportsDao) {
        this.reportsDao = reportsDao;
    }

    public void display() {
        boolean running = true;

        while (running) {
            InputValidator.clearScreen();
            displayMenu();

            int choice = InputValidator.getIntInRange("Select an option: ", 1, 4);

            switch (choice) {
                case 1:
                    showMangoMusicMapped();
                    break;
                case 2:
                    showMostPlayedGenreByAlbum();

                    break;
                case 3:
                    //@TODO - Create report
                    showUserDiversityScore();
                    break;
                case 4:
                    //@TODO - Create report
//                    showPeakListeningHours();
                    break;
                case 0:
                    running = false;
                    break;
            }
        }
    }

    private void showUserDiversityScore() {
        InputValidator.clearScreen();
        ConsoleColors.printSection("Most Played Album By Genre");
        List<ReportResult> results = reportsDao.getDiversityReport();

        if(results.isEmpty()){
            ConsoleColors.printSection("No data.");
        } else {
            System.out.printf("%-20s %-30s %10s%n",
                    "Country", "User", "Percentage of Total User");
            System.out.println("-".repeat(125));

            int displayCount = Math.min(results.size(), 30);
            for(int i = 0; i < displayCount; i++){
                ReportResult result = results.get(i);
                String percentageStr = result.getString("percentage");
                double parsePercentage = Double.parseDouble(percentageStr);
                System.out.printf("%-20s %-30s %10.2f%%%n",
                        result.getString("country"),
                        result.getInt("user_count"), parsePercentage);

            }

            if (results.size() > 30) {
                System.out.println("\n... and " + (results.size() - 30) + " more users at risk");
            }

        }
        InputValidator.pressEnterToContinue();
    }

    private void showMostPlayedGenreByAlbum() {
        InputValidator.clearScreen();
        ConsoleColors.printSection("Most Played Album By Genre");
        List<ReportResult> results = reportsDao.getMostPlayedAlbumsByGenre();

        if(results.isEmpty()){
            ConsoleColors.printSection("No data.");
        } else {
            System.out.printf("%-20s %-30s %10s%n",
                    "Primary Genre", "Play Time", "Album Title");
            System.out.println("-".repeat(125));

            int displayCount = Math.min(results.size(), 30);
            for(int i = 0; i < displayCount; i++){
                ReportResult result = results.get(i);
                System.out.printf("%-20s %-30s %10s%n",
                    result.getString("primary_genre"),
                    result.getInt("play_amount"),
                    result.getString("album_title"));
            }

            if (results.size() > 30) {
                System.out.println("\n... and " + (results.size() - 30) + " more users at risk");
            }

        }
        InputValidator.pressEnterToContinue();
    }

    private void displayMenu() {
        ConsoleColors.printHeader("SPECIAL REPORTS");

        System.out.println("\nPERSONALIZED ANALYTICS:");
        System.out.println("1. MangoMusic Mapped (Year in Review)");
        System.out.println("2. Most Played Albums by Genre");
        System.out.println("3. User Listening Diversity Score");
        System.out.println("4. Peak Listening Hours Analysis");

        System.out.println("\n0. Back to main menu");
        System.out.println();
    }

    private void showMangoMusicMapped() {
        InputValidator.clearScreen();
        ConsoleColors.printHeader("🎵 MANGOMUSIC MAPPED 🎵");
        System.out.println("Your personalized year in review\n");

        int userId = InputValidator.getIntInRange("Enter user ID: ", 1, Integer.MAX_VALUE);

        ReportResult mapped = reportsDao.getMangoMusicMapped(userId);

        int year = mapped.getInt("year");

        if (mapped.getInt("total_plays") == 0) {
            ConsoleColors.printWarning("No listening data found for user ID " + userId + " in " + year + ".");
        } else {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("YOUR " + year + " LISTENING STORY");
            System.out.println("=".repeat(70));

            System.out.println("\n🎧 LISTENING STATS:");
            System.out.println("   Total Plays: " + mapped.getInt("total_plays"));
            System.out.println("   Albums Explored: " + mapped.getInt("unique_albums"));
            System.out.println("   Artists Discovered: " + mapped.getInt("unique_artists"));
            System.out.println("   Completion Rate: " +
                    String.format("%.1f%%", (mapped.getInt("completed_plays") * 100.0 / mapped.getInt("total_plays"))));

            System.out.println("\n⭐ YOUR TOP PICKS:");
            System.out.println("   #1 Artist: " + mapped.getString("top_artist") +
                    " (" + mapped.getInt("top_artist_plays") + " plays)");
            System.out.println("   Favorite Genre: " + mapped.getString("top_genre"));
            System.out.println("   Most Active Month: " + mapped.getString("top_month") +
                    " (" + mapped.getInt("top_month_plays") + " plays)");

            System.out.println("\n🔥 FUN FACTS:");
            System.out.println("   Longest Listening Streak: " + mapped.getInt("longest_streak") + " days");
            System.out.println("   Listener Personality: " + mapped.getString("listener_personality"));

            System.out.println("\n" + "=".repeat(70));
            System.out.println("Thanks for making " + year + " a year full of music! 🎶");
            System.out.println("=".repeat(70));
        }

        InputValidator.pressEnterToContinue();
    }
}