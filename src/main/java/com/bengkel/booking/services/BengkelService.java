package com.bengkel.booking.services;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class BengkelService {
	
	//Silahkan tambahkan fitur-fitur utama aplikasi disini
	private static DecimalFormat rupiah = new DecimalFormat("#,###");
	
	//Login
	public static Customer login(List<Customer> listAllCustomers) {
		Customer loggedCustomer = new Customer();
		
		String customerId = Validation.validasiUserName("Masukkan Customer ID : ", "Username Tidak Ditemukan!", listAllCustomers, "^C.+$");
		boolean isPasswordValid = Validation.validasiPassword("Masukkan Password : ", "Password Salah!", listAllCustomers, customerId, "^[a-zA-Z0-9]+$");
		
		if(isPasswordValid) {
			loggedCustomer = listAllCustomers.stream()
				    			.filter(customer -> customer.getCustomerId().equalsIgnoreCase(customerId))
				    			.findFirst()
				    			.orElse(null);
		} else {
			System.out.println("Melewati batas mencoba login. Aplikasi akan ditutup!");
			System.exit(1);
		}
		
		return loggedCustomer;
	}
	
	//Info Customer
	
	//Booking atau Reservation
	public static BookingOrder setBookingOrder(Customer customer, List<ItemService> listAllItemService, List<BookingOrder> listBookingOrder) {
		BookingOrder bookingOrder = new BookingOrder();
		String vehicleId = "";
		String vehicleType = "";
		String serviceId = "";
		String metodePembayaran = "";
		
		double totalServicePrice = 0.0;
		
		LocalDate bookingDate;
		
		List<ItemService> choosenService = new ArrayList<ItemService>();
		List<String> listServiceId = new ArrayList<String>();
		
		PrintService.printVechicle(customer.getVehicles());
		vehicleId = Validation.validasiVehicleId("Masukkan Vehicle ID : ", "Vehicle ID tidak ditemukan!", customer.getVehicles());
		for(Vehicle v : customer.getVehicles()) {
			if(v.getVehiclesId().equalsIgnoreCase(vehicleId)) {
				vehicleType = v.getVehicleType();
				break;
			}
		}
		
		PrintService.printAllServices(vehicleType, listAllItemService);
		if(customer instanceof MemberCustomer) {
			String tambah = "";
			
			boolean isDone = false;
			
			int max = 0;
			double sisaSaldo = 0.0;
			
			do {
				serviceId = Validation.validasiServiceUnique(listServiceId, "Masukkan Service ID : ", "Service ID Tidak Ditemukan!", listAllItemService, vehicleType);
				for(ItemService service : listAllItemService) {
					if(service.getServiceId().equalsIgnoreCase(serviceId)) {
						listServiceId.add(serviceId);
						choosenService.add(service);
					}
				}
				max++;
				if(max >= 2)break;
				tambah = Validation.validasiInput("Apakah Anda ingin menambahkan service lainnya? (Y/T) : ", "Input tidak valid! Hanya bisa Y/T", "^[ytYT]+$");
				if(tambah.equalsIgnoreCase("T")) {
					isDone = true;
				}
			}while(max<2 && !isDone);
			
			String bookingId = "Book-Cust-" + String.format("%03d", listBookingOrder.size()+1) + "-" + customer.getCustomerId().substring(5);
			
			metodePembayaran = Validation.validasiMetodePembayaran("Silahkan pilih metode pembayaran (Saldo Coin atau Cash) : ", "Hanya bisa menggunakan saldo coin atau cash", "^[a-zA-Z\\s]+$");
			
			bookingDate = LocalDate.now();
			
			
			for(ItemService service : choosenService) {
				totalServicePrice += service.getPrice();
			}
			
			bookingOrder = new BookingOrder(bookingId, customer, choosenService, metodePembayaran, totalServicePrice, bookingDate);
			bookingOrder.calculatePayment();
			
			System.out.println("Saldo sekarang : " + ((MemberCustomer) customer).getSaldoCoin());
			
			if(metodePembayaran.equalsIgnoreCase("saldo coin")) {
				if(((MemberCustomer) customer).getSaldoCoin() < bookingOrder.getTotalServicePrice()) {
					System.out.println("Saldo Coin kurang. Pembayaran akan otomatis menggunakan cash");
					metodePembayaran = "Cash";
				} else {
					sisaSaldo = ((MemberCustomer) customer).getSaldoCoin() - bookingOrder.getTotalPayment();
					((MemberCustomer) customer).setSaldoCoin(sisaSaldo);
				}
			}
		} else {
			
			serviceId = Validation.validasiServiceUnique(listServiceId, "Masukkan Service ID : ", "Service ID Tidak Ditemukan!", listAllItemService, vehicleType);
			for(ItemService service : listAllItemService) {
				if(service.getServiceId().equalsIgnoreCase(serviceId)) {
					listServiceId.add(serviceId);
					choosenService.add(service);
				}
			}
			
			String bookingId = "Book-Cust-" + String.format("%03d", listBookingOrder.size()+1) + "-" + customer.getCustomerId().substring(5);
			
			metodePembayaran = "Cash";
			
			bookingDate = LocalDate.now();
			
			for(ItemService service : choosenService) {
				totalServicePrice += service.getPrice();
			}
			
			bookingOrder = new BookingOrder(bookingId, customer, choosenService, metodePembayaran, totalServicePrice, bookingDate);
			bookingOrder.calculatePayment();
			
		}
		
		System.out.println("Booking Berhasil!");
		System.out.println(String.format("%-30s", "Total Harga Service : ") + "Rp." + rupiah.format(bookingOrder.getTotalServicePrice()).replace(',', '.'));
		System.out.println(String.format("%-30s", "Total Pembayaran : ") + "Rp." + rupiah.format(bookingOrder.getTotalPayment()).replace(',', '.'));
		
		return bookingOrder;
	}
	
	//Top Up Saldo Coin Untuk Member Customer
	public static void topUpSaldo(Customer customer) {
		double topUpAmount = 0.0;
		double total = 0.0;
		
		topUpAmount = Double.valueOf(Validation.validasiInput("Masukkan Jumlah Top Up : ", "Input Invalid! Hanya bisa input angka", "^[0-9]+$"));
		
		total = ((MemberCustomer) customer).getSaldoCoin() + topUpAmount;
		
		((MemberCustomer) customer).setSaldoCoin(total);
	}
	
	//Logout
	
}
