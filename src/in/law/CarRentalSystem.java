package in.law;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CarRentalSystem {
	private List<Car> cars;
	private List<Customer> customers;
	private List<Rental> rentals;

	public CarRentalSystem() {
		cars = new LinkedList<>();
		customers = new LinkedList<>();
		rentals = new LinkedList<>();
	}

	public void addCar(Car car) {
		cars.add(car);
	}

	public void addCustomer(Customer customer) {
		customers.add(customer);
	}

	public void rentCar(Car car, Customer customer, int days) {
		car.rent();
		rentals.add(new Rental(car, customer, days));
	}

	public void returnCar(Car car) {
		car.returnCar();
		Rental rentalToRemove = null;
		for (Rental rental : rentals) {
			if (rental.getCar() == car) {
				rentalToRemove = rental;
				break;
			}
		}
		if (rentalToRemove != null) {
			rentals.remove(rentalToRemove);

		} else {
			System.out.println("Car was not rented.");
		}
	}

	public void menu() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n===== Car Rental System =====");
			System.out.println("1. Rent a Car");
			System.out.println("2. Return a Car");
			System.out.println("3. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			int size = 0;

			if (choice == 1) {
				System.out.println("\n== Rent a Car ==");
				for (Car car : cars) {
					if (car.isAvailable())
						size++;
				}
				System.out.println("Available Cars : " + size);
				if (size > 0) {
					for (Car car : cars) {
						if (car.isAvailable()) {
							System.out.println("[carID:" + car.getCarId() + " , Brand:" + car.getBrand() + " , Model:"
									+ car.getModel() + "]");
						}
					}

					System.out.print("\nConfirm rental (Yes/No): ");
					String confirm = scanner.nextLine();
					if (confirm.equalsIgnoreCase("YES")) {
						System.out.print("Enter your name: ");
						String customerName = scanner.nextLine();

						Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
						addCustomer(newCustomer);

						System.out.print("\nEnter the car ID you want to rent: ");
						String carId = scanner.nextLine();

						Car selectedCar = null;
						boolean isIdPresent = false;
						for (Car car : cars) {
							if (car.getCarId().equals(carId)) {
								isIdPresent = true;
								selectedCar = car;
								break;
							}
						}
						if (isIdPresent) {
							System.out.print("Enter the number of days for rental: ");
							int rentalDays = scanner.nextInt();
							scanner.nextLine(); // Consume newline

							rentCar(selectedCar, newCustomer, rentalDays);
							System.out.println("Enjoy ! , Car rented successfully.");

							double totalPrice = selectedCar.calculatePrice(rentalDays);
							System.out.println("<== Rental Information ==>");
							System.out.println("Customer ID: " + newCustomer.getCustomerId());
							System.out.println("Customer Name: " + newCustomer.getName());
							System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
							System.out.println("Rental Days: " + rentalDays);
							System.out.printf("Total Price: Rs.%.2f%n", totalPrice);
						} else {
							System.out.println("Invalid carId");
						}
					} else {
						System.out.println("\nRental canceled.");
					}
				} else {
					System.out.println("No Cars available");
				}

			} else if (choice == 2) {

				System.out.println("\n== Return a Car ==");
				System.out.print("Enter the car ID you want to return: ");
				String carId = scanner.nextLine();

				Car carToReturn = null;

				for (Car car : cars) {
					if (car.getCarId().equals(carId)) {
						car.returnCar();
						carToReturn = car;
						break;
					}
				}

				if (carToReturn != null) {
					Customer customer = null;
					for (Rental rental : rentals) {
						if (rental.getCar() == carToReturn) {
							customer = rental.getCustomer();
							break;
						}
					}

					if (customer != null) {
						returnCar(carToReturn);
						System.out.println("Car returned successfully by " + customer.getName());
					} else {
						System.out.println("Car was not rented or rental information is missing.");
					}
				} else {
					System.out.println("Invalid car ID or car is not rented.");
				}
			} else if (choice == 3) {
				break;
			} else {
				System.out.println("Invalid choice. Please enter a valid option.");
			}
		}

		System.out.println("\nThank you for using the Car Rental System!");
		scanner.close();
	}

}
