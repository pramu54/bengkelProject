package com.bengkel.booking.services;

import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.Vehicle;

public class Validation {
	
	public static String validasiInput(String question, String errorMessage, String regex) {
	    Scanner input = new Scanner(System.in);
	    String result;
	    boolean isLooping = true;
	    do {
	      System.out.print(question);
	      result = input.nextLine();

	      //validasi menggunakan matches
	      if (result.matches(regex)) {
	        isLooping = false;
	      }else {
	        System.out.println(errorMessage);
	      }

	    } while (isLooping);

	    return result;
	}
	
	public static int validasiNumberWithRange(String question, String errorMessage, String regex, int max, int min) {
	    int result;
	    boolean isLooping = true;
	    do {
	      result = Integer.valueOf(validasiInput(question, errorMessage, regex));
	      if (result >= min && result <= max) {
	        isLooping = false;
	      }else {
	        System.out.println("Pilihan angka " + min + " s.d " + max);
	      }
	    } while (isLooping);

	    return result;
	}
	
	public static String validasiUserName(String question, String errorMessage, List<Customer> listAllCustomers, String regex) {
		String userName;
		boolean isValid = false;
		
		do {
			userName = validasiInput(question, "Input tidak valid. Hanya bisa huruf dan angka", regex);
			final String userTemp = userName;
			
			isValid = listAllCustomers.stream()
						.anyMatch(customer -> customer.getCustomerId().equalsIgnoreCase(userTemp));
			
			if(!isValid) {
				System.out.println(errorMessage);
			}
			
		}while(!isValid);
		
		return userName;
	}
	
	public static boolean validasiPassword(String question, String errorMessage, List<Customer> listAllCustomers, String customerId, String regex) {
		String password;
		boolean isValid = false;
		int attempts = 0;
		
		do {
			password = validasiInput(question, "Input tidak valid. Hanya bisa huruf dan angka", regex);
			final String passwordTemp = password;
			
			isValid = listAllCustomers.stream()
						.filter(customer -> customer.getCustomerId().equalsIgnoreCase(customerId))
						.map(customer -> customer)
						.anyMatch(customer -> customer.getPassword().equalsIgnoreCase(passwordTemp));
			
			if(!isValid) {
				System.out.println(errorMessage);
				attempts++;
			} else {
				System.out.println();
				System.out.println("Login Berhasil!");
				System.out.println();
			}
			
		}while(!isValid && attempts < 3);
		
		return isValid;
	}
	
	public static String validasiVehicleId(String question, String errorMessage, List<Vehicle> listVehicle) {
		String vehicleId = "";
		boolean isValid = false;
		
		do {
			vehicleId = validasiInput(question, "Input tidak valid. Hanya bisa huruf dan angka", "^[A-Z0-9]+$");
			final String vehicleTemp = vehicleId;
			
			isValid = listVehicle.stream()
						.anyMatch(customer -> customer.getVehiclesId().equalsIgnoreCase(vehicleTemp));
			
			if(!isValid) {
				System.out.println(errorMessage);
			} 
			
		}while(!isValid);
		
		return vehicleId;
	}
	
	public static String validasiServiceId(String question, String errorMessage, List<ItemService> listService, String vehicleType) {
		String serviceId = "";
		boolean isValid = false;
		
		do {
			serviceId = validasiInput(question, "Input tidak valid. Hanya bisa huruf dan angka", "^S.+$");
			final String serviceTemp = serviceId;
			
			isValid = listService.stream()
						.anyMatch(service -> service.getServiceId().equalsIgnoreCase(serviceTemp));
			
			if(!isValid) {
				System.out.println(errorMessage);
			} 
			
		}while(!isValid);
		
		return serviceId;
	}
	
	public static String validasiServiceUnique(List<String> listServiceId, String question, String errorMessage, List<ItemService> listService, String vehicleType) {
		boolean isNotUnique = false;
		String serviceId = "";
		
			do {
				serviceId = validasiServiceId(question, errorMessage, listService, vehicleType);
				final String serviceTemp = serviceId;
				
				if(!listServiceId.isEmpty()) {
				
					isNotUnique = listServiceId.stream()
									.anyMatch(service -> service.equalsIgnoreCase(serviceTemp));
					
					if(isNotUnique) {
						System.out.println("Service sudah dipilih. Silahkan pilih yang lain!");
					}
				}
			}while(isNotUnique);
		
		return serviceId;
	}
	
	public static String validasiMetodePembayaran(String question, String errorMessage, String regex) {
		String metodePembayaran = "";
		boolean isValid = false;
		
		do {
			metodePembayaran = validasiInput(question, "Input tidak valid. Hanya bisa menggunakan huruf", regex);
			
			if(metodePembayaran.equalsIgnoreCase("Saldo Coin") || metodePembayaran.equalsIgnoreCase("Cash")) {
				isValid = true;
			}
		}while(!isValid);
		
		return metodePembayaran;
	}
}
