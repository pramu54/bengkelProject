package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.repositories.CustomerRepository;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
	private static Scanner input = new Scanner(System.in);
	private static List<Customer> userLoggedIn = new ArrayList<Customer>();
	private static List<BookingOrder> listBookingOrder = new ArrayList<BookingOrder>();
	
	public static void run() {
		boolean isLooping = true;
		do {
			login();
			mainMenu();
		} while (isLooping);
		
	}
	
	public static void login() {
		String[] loginMenu = {"Login", "Exit"};
		int loginMenuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(loginMenu, "Aplikasi Bengkel Prime");
			loginMenuChoice = Validation.validasiNumberWithRange("Masukkan Pilihan Menu: ", "Input Harus Berupa Angka!", "^[0-9]+$", loginMenu.length-1, 0);
			System.out.println(loginMenuChoice);
			
			switch (loginMenuChoice) {
			case 1:
				userLoggedIn.add(BengkelService.login(listAllCustomers));
				break;
			case 0:
				System.out.println("Terima kasih telah menggunakan aplikasi Aplikasi Bengkel Prime.");
				System.exit(1);
				break;
			default:
				System.out.println("Menu Tidak Tersedia!!!");
			}
		}while(!isLooping);
	}
	
	public static void mainMenu() {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu : ", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);
			
			switch (menuChoice) {
			case 1:
				PrintService.printCustomerInfo(userLoggedIn.get(0));
				break;
			case 2:
				//panggil fitur Booking Bengkel
				listBookingOrder.add(BengkelService.setBookingOrder(userLoggedIn.get(0), listAllItemService, listBookingOrder));
				break;
			case 3:
				//panggil fitur Top Up Saldo Coin
				if(userLoggedIn.get(0) instanceof MemberCustomer) {
					BengkelService.topUpSaldo(userLoggedIn.get(0));
				} else {
					System.out.println("Maaf. Fitur ini hanya tersedia untuk Member!");
				}
				break;
			case 4:
				//panggil fitur Informasi Booking Order
				PrintService.printAllBookingOrder(userLoggedIn.get(0).getCustomerId(), listBookingOrder);
				break;
			case 0:
				System.out.println("Logout");
				
				userLoggedIn.clear();
				
				isLooping = false;
				break;
			default:
				System.out.println("Menu Tidak Tersedia!!!");
			}
		} while (isLooping);
		
		
	}
	
	//Silahkan tambahkan kodingan untuk keperluan Menu Aplikasi
}
