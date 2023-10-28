package com.bengkel.booking.services;

import java.text.DecimalFormat;
import java.util.List;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

public class PrintService {
	private static DecimalFormat rupiah = new DecimalFormat("#,###");
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";
		
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}
	
	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Vechicle Id", "Warna", "Brand", "Transmisi", "Tahun", "Tipe Kendaraan");
	    System.out.format(line);
	    int number = 1;
	    String vehicleType = "";
	    for (Vehicle vehicle : listVehicle) {
	    	if (vehicle instanceof Car) {
				vehicleType = "Mobil";
			}else {
				vehicleType = "Motor";
			}
	    	System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(), vehicle.getTransmisionType(), vehicle.getYearRelease(), vehicleType);
	    	number++;
	    }
	    System.out.printf(line);
	}
	
	public static void printCustomerInfo(Customer customer) {
		System.out.println(String.format("%-30s", "Customer Id") + customer.getCustomerId());
		System.out.println(String.format("%-30s", "Nama") + customer.getName());
		if(customer instanceof MemberCustomer) {
			System.out.println(String.format("%-30s", "Customer Status") + "Member");
			System.out.println(String.format("%-30s", "Alamat") + customer.getAddress());
			System.out.println(String.format("%-30s", "Saldo") + "Rp." + rupiah.format(((MemberCustomer) customer).getSaldoCoin()).replace(',', '.'));
		} else {
			System.out.println(String.format("%-30s", "Customer Status") + "Non-Member");
			System.out.println(String.format("%-30s", "Alamat") + customer.getAddress());
			System.out.println(String.format("%-30s", "Saldo") + "-");
		}
		System.out.println();
		System.out.println("List Kendaraan : ");
		printVechicle(customer.getVehicles());
	}
	
	public static void printAllServices(String vehicleType, List<ItemService> listAllService) {
		String formatTable = "| %-2s | %-15s | %-15s | %-15s | %-15s |%n";
		String line = "+----+-----------------+-----------------+-----------------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Service Id", "Nama Service", "Tipe Kendaraan", "Harga");
	    System.out.format(line);
	    int number = 1;
	    
	    for (ItemService service : listAllService) {
			if(service.getVehicleType().equalsIgnoreCase(vehicleType)) {
				System.out.format(formatTable, number, service.getServiceId(), service.getServiceName(), service.getVehicleType(), "Rp." + rupiah.format(service.getPrice()).replace(',', '.'));
				number++;
			}
		}
	    System.out.printf(line);
	    String formatAkhir = "| %-2s | %-69s | %n";
	    System.out.format(formatAkhir, 0, "Kembali Ke Home Menu");
	    System.out.printf(line);
	}
	
	public static void printAllBookingOrder(String customerId, List<BookingOrder> listAllBookingOrder) {
		String formatTable = "| %-2s | %-30s | %-20s | %-20s | %-20s | %-20s | %-50s | %-20s |%n";
		String line = "+----+--------------------------------+----------------------+----------------------+----------------------+----------------------+----------------------------------------------------+----------------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Booking Id", "Nama Customer", "Payment Method", "Total Service", "Total Payment", "List Service", "Tanggal Booking");
	    System.out.format(line);
	    int number = 1;
	    
	    for (BookingOrder booking : listAllBookingOrder) {
	    	String customerBooking = booking.getBookingId().substring(5, 9) + booking.getBookingId().substring(13);
			if(customerBooking.equalsIgnoreCase(customerId)) {
				System.out.format(formatTable, number, booking.getBookingId(), booking.getCustomer().getName(), booking.getPaymentMethod(), "Rp." + rupiah.format(booking.getTotalServicePrice()).replace(',', '.'), "Rp." + rupiah.format(booking.getTotalPayment()).replace(',', '.'), printListService(booking.getServices()), booking.getBookingDate());
				number++;
			}
		}
	    System.out.printf(line);
	}
	
	public static String printListService(List<ItemService> chosenService) {
		String services = "";
		
		for (ItemService service : chosenService) {
			services += service.getServiceName() + ",";
		}
		
		return services;
	}
	
	//Silahkan Tambahkan function print sesuai dengan kebutuhan.
	
}
