package com.example.managebyclesystem;

import com.example.managebyclesystem.constants.*;
import com.example.managebyclesystem.model.*;
import com.example.managebyclesystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private BikeRepository bikeRepository;
    @Autowired
    private PromotionRepo promotionRepo;
    @Autowired
    private MaintenanceRepo maintenanceRepo;
    @Autowired
    private RentalOrderRepository rentalOrderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Staff (có kiểm tra trùng lặp)
        seedStaff();

        // 2. Seed các bảng không phụ thuộc
        seedCustomers();
        seedBikes();
        seedPromotions();

        // 3. Seed các bảng phụ thuộc (sau khi đã có bike, customer, promotion)
        seedMaintenances();
        seedRentalOrdersAndPayments();

        System.out.println(">>> Data seeding complete.");
    }

    private void seedStaff() {
        System.out.println(">>> Seeding staff...");
        if (staffRepository.findByUsername("admin").isEmpty()) {
            Staff adminUser = new Staff();
            adminUser.setStaffName("Admin User");
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(StaffRole.ROLE_ADMIN);
            adminUser.setStaffStatus(Staff.StaffStatus.Able);
            adminUser.setStaffPosition(Staff.StaffPosition.MANAGER);
            adminUser.setStaffSalary(15000000);
            adminUser.setStaffShift(Staff.StaffShift.FULLDAY);
            staffRepository.save(adminUser);
        }

        if (staffRepository.findByUsername("user").isEmpty()) {
            Staff normalUser = new Staff();
            normalUser.setStaffName("Normal User");
            normalUser.setUsername("user");
            normalUser.setPassword(passwordEncoder.encode("user123"));
            normalUser.setRole(StaffRole.ROLE_USER);
            normalUser.setStaffStatus(Staff.StaffStatus.Able);
            normalUser.setStaffPosition(Staff.StaffPosition.STAFF);
            normalUser.setStaffSalary(7000000);
            normalUser.setStaffShift(Staff.StaffShift.MORNING);
            staffRepository.save(normalUser);
        }

        // Thêm nhiều nhân viên hơn
        if (staffRepository.count() < 20) {
            List<Staff> staffList = Arrays.asList(
                    createStaff("Trần Văn Bảo", "baotran", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.AFTERNOON, 7200000, StaffRole.ROLE_USER),
                    createStaff("Lê Thị Cẩm", "camle", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.MORNING, 7000000, StaffRole.ROLE_USER),
                    createStaff("Phan Minh Đức", "ducphan", "pass123", Staff.StaffPosition.MAINTENANCE, Staff.StaffShift.FULLDAY, 9000000, StaffRole.ROLE_USER),
                    createStaff("Võ Hoài An", "anvo", "pass123", Staff.StaffPosition.SECURITY, Staff.StaffShift.EVENING, 6500000, StaffRole.ROLE_USER),
                    createStaff("Đặng Quốc Hưng", "hungdang", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.MORNING, 7100000, StaffRole.ROLE_USER),
                    createStaff("Ngô Thanh Vân", "vanngo", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.AFTERNOON, 7300000, StaffRole.ROLE_USER),
                    createStaff("Hồ Trọng Tín", "tinho", "pass123", Staff.StaffPosition.MAINTENANCE, Staff.StaffShift.FULLDAY, 9100000, StaffRole.ROLE_USER),
                    createStaff("Dương Tiểu Long", "longduong", "pass123", Staff.StaffPosition.SECURITY, Staff.StaffShift.MORNING, 6600000, StaffRole.ROLE_USER),
                    createStaff("Phạm Thị Mai", "maipham", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.EVENING, 7500000, StaffRole.ROLE_USER),
                    createStaff("Bùi Quang Vinh", "vinhbui", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.FULLDAY, 8000000, StaffRole.ROLE_USER),
                    createStaff("Hoàng Văn Nam", "namhoang", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.MORNING, 7000000, StaffRole.ROLE_USER),
                    createStaff("Tống Mỹ Linh", "linhtong", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.AFTERNOON, 7200000, StaffRole.ROLE_USER),
                    createStaff("Chu Văn Kiên", "kienchu", "pass123", Staff.StaffPosition.MAINTENANCE, Staff.StaffShift.FULLDAY, 9200000, StaffRole.ROLE_USER),
                    createStaff("Lý Hoàng Phúc", "phucly", "pass123", Staff.StaffPosition.SECURITY, Staff.StaffShift.EVENING, 6700000, StaffRole.ROLE_USER),
                    createStaff("Đỗ Hùng Dũng", "dungdo", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.MORNING, 7100000, StaffRole.ROLE_USER),
                    createStaff("Vương Thu Thủy", "thuyvuong", "pass123", Staff.StaffPosition.STAFF, Staff.StaffShift.AFTERNOON, 7300000, StaffRole.ROLE_USER),
                    createStaff("Nguyễn Tiến Linh", "linhnguyen", "pass123", Staff.StaffPosition.MAINTENANCE, Staff.StaffShift.FULLDAY, 9300000, StaffRole.ROLE_USER),
                    createStaff("Trịnh Văn Quyết", "quyettv", "pass123", Staff.StaffPosition.SECURITY, Staff.StaffShift.MORNING, 6800000, StaffRole.ROLE_USER)
            );
            staffRepository.saveAll(staffList);
        }
    }

    private Staff createStaff(String name, String username, String pass, Staff.StaffPosition pos, Staff.StaffShift shift, double salary, StaffRole role) {
        Staff s = new Staff();
        s.setStaffName(name);
        s.setUsername(username);
        s.setPassword(passwordEncoder.encode(pass));
        s.setStaffPosition(pos);
        s.setStaffShift(shift);
        s.setStaffSalary(salary);
        s.setRole(role);
        s.setStaffStatus(Staff.StaffStatus.Able);
        return s;
    }

    private void seedCustomers() {
        if (customerRepo.count() == 0) {
            System.out.println(">>> Seeding customers...");
            List<Customer> customers = Arrays.asList(
                    new Customer("Nguyễn Văn A", "0912345678", "a.nguyen@example.com"),
                    new Customer("Trần Thị B", "0987654321", "b.tran@example.com"),
                    new Customer("Lê Văn C", "0905111222", "c.le@example.com", 150, CustomerCardType.SILVER),
                    new Customer("Phạm Thị D", "0935333444", "d.pham@example.com"),
                    new Customer("Hoàng Văn E", "0945555666", "e.hoang@example.com", 550, CustomerCardType.GOLD),
                    new Customer("Vũ Thị F", "0977777888", "f.vu@example.com"),
                    new Customer("Đỗ Văn G", "0966999000", "g.do@example.com", 1200, CustomerCardType.DIAMOND),
                    new Customer("Bùi Thị H", "0918222333", "h.bui@example.com"),
                    new Customer("Ngô Văn I", "0988444555", "i.ngo@example.com", 50, CustomerCardType.BROWN),
                    new Customer("Dương Thị K", "0906777888", "k.duong@example.com"),
                    new Customer("Lý Văn L", "0917333999", "l.ly@example.com"),
                    new Customer("Mai Thị M", "0989111000", "m.mai@example.com", 320, CustomerCardType.SILVER),
                    new Customer("Tô Văn N", "0903888777", "n.to@example.com"),
                    new Customer("Châu Thị O", "0934666555", "o.chau@example.com"),
                    new Customer("Tạ Văn P", "0949444333", "p.ta@example.com", 800, CustomerCardType.GOLD),
                    new Customer("Kiều Thị Q", "0975222111", "q.kieu@example.com"),
                    new Customer("Hà Văn R", "0963000999", "r.ha@example.com"),
                    new Customer("Mạc Thị S", "0916555444", "s.mac@example.com"),
                    new Customer("Đinh Văn T", "0984333222", "t.dinh@example.com", 20, CustomerCardType.BROWN),
                    new Customer("Giang Thị U", "0908111999", "u.giang@example.com")
            );
            customerRepo.saveAll(customers);
        }
    }

    private void seedBikes() {
        if (bikeRepository.count() == 0) {
            System.out.println(">>> Seeding bikes...");
            List<Bike> bikes = Arrays.asList(
                    createBike("Xe Thống Nhất (Nữ)", Bike.BikeType.NORMAL, 15000, Bike.BikeStatus.Available, "/uploads/bikes/1761597466716_bike2.jpg"),
                    createBike("Xe địa hình Fornix", Bike.BikeType.NORMAL, 20000, Bike.BikeStatus.Available, "/uploads/bikes/1761315999120_bike3.jpg"),
                    createBike("Xe điện Giant (Trắng)", Bike.BikeType.ELECTRIC, 35000, Bike.BikeStatus.Available, "/uploads/bikes/sample1.jpg"),
                    createBike("Xe trẻ em (Hồng)", Bike.BikeType.KID, 10000, Bike.BikeStatus.Maintenance, "/uploads/bikes/1761316396695_cmc.png"),
                    createBike("Xe đạp Asama (Đen)", Bike.BikeType.NORMAL, 18000, Bike.BikeStatus.Available, null),
                    createBike("Xe điện Hitasa (Đỏ)", Bike.BikeType.ELECTRIC, 30000, Bike.BikeStatus.Available, null),
                    createBike("Xe trẻ em (Xanh)", Bike.BikeType.KID, 10000, Bike.BikeStatus.Available, null),
                    createBike("Xe địa hình Giant", Bike.BikeType.NORMAL, 25000, Bike.BikeStatus.Available, null),
                    createBike("Xe đạp mini (Trắng)", Bike.BikeType.NORMAL, 15000, Bike.BikeStatus.Available, null),
                    createBike("Xe điện Pega", Bike.BikeType.ELECTRIC, 32000, Bike.BikeStatus.Unavailable, null), // Đang được thuê
                    createBike("Xe đạp đua (Xám)", Bike.BikeType.NORMAL, 30000, Bike.BikeStatus.Available, null),
                    createBike("Xe trẻ em (Vàng)", Bike.BikeType.KID, 10000, Bike.BikeStatus.Available, null),
                    createBike("Xe địa hình Trinx", Bike.BikeType.NORMAL, 22000, Bike.BikeStatus.Available, null),
                    createBike("Xe điện Vinfast (Bạc)", Bike.BikeType.ELECTRIC, 40000, Bike.BikeStatus.Available, null),
                    createBike("Xe đạp gấp (Đen)", Bike.BikeType.NORMAL, 20000, Bike.BikeStatus.Available, null),
                    createBike("Xe đạp đôi (Xanh)", Bike.BikeType.NORMAL, 40000, Bike.BikeStatus.Maintenance, null),
                    createBike("Xe trẻ em (Cam)", Bike.BikeType.KID, 10000, Bike.BikeStatus.Available, null),
                    createBike("Xe địa hình Galaxy", Bike.BikeType.NORMAL, 20000, Bike.BikeStatus.Available, null),
                    createBike("Xe điện Yadea (Trắng)", Bike.BikeType.ELECTRIC, 33000, Bike.BikeStatus.Available, null),
                    createBike("Xe đạp Martin", Bike.BikeType.NORMAL, 16000, Bike.BikeStatus.Available, null)
            );
            bikeRepository.saveAll(bikes);
        }
    }

    private Bike createBike(String name, Bike.BikeType type, double rent, Bike.BikeStatus status, String img) {
        Bike b = new Bike();
        b.setBikeName(name);
        b.setBikeType(type);
        b.setBikeRentPerHour(rent);
        b.setBikeStatus(status);
        b.setBikeImage(img);
        b.setBikeActiveStatus(Bike.ActiveStatus.ABLE);
        return b;
    }

    private void seedPromotions() {
        if (promotionRepo.count() == 0) {
            System.out.println(">>> Seeding promotions...");
            LocalDate today = LocalDate.now();
            List<Promotion> promotions = Arrays.asList(
                    createPromotion("Giảm 10%", PromotionType.PERCENTAGE, 10, today.minusDays(5), today.plusDays(10), PromotionStatus.ABLE),
                    createPromotion("Giảm 5,000đ", PromotionType.FIXED_AMOUNT, 5000, today, today.plusDays(30), PromotionStatus.ABLE),
                    createPromotion("Khai trương giảm 20%", PromotionType.PERCENTAGE, 20, today.minusDays(1), today.plusDays(5), PromotionStatus.ABLE),
                    createPromotion("Giảm 15,000đ cuối tuần", PromotionType.FIXED_AMOUNT, 15000, today.plusDays(3), today.plusDays(5), PromotionStatus.ABLE),
                    createPromotion("Khuyến mãi hè", PromotionType.PERCENTAGE, 15, today.minusDays(10), today.plusDays(20), PromotionStatus.ABLE),
                    createPromotion("Hết hạn", PromotionType.PERCENTAGE, 50, today.minusDays(10), today.minusDays(1), PromotionStatus.DISABLE) // Hết hạn
            );
            promotionRepo.saveAll(promotions);
        }
    }

    private Promotion createPromotion(String name, PromotionType type, double discount, LocalDate start, LocalDate end, PromotionStatus status) {
        Promotion p = new Promotion();
        p.setPromotionName(name);
        p.setPromotionType(type);
        p.setPromotionDiscount(discount);
        p.setPromotionStartDate(start);
        p.setPromotionEndDate(end);
        p.setPromotionStatus(status);
        return p;
    }

    private void seedMaintenances() {
        // *** SỬA LỖI: Chỉ thực thi nếu `maintenanceRepo` trống VÀ `bikeRepository` có dữ liệu ***
        if (maintenanceRepo.count() == 0 && bikeRepository.count() > 0) {
            System.out.println(">>> Seeding maintenances...");
            List<Bike> bikes = bikeRepository.findAll();

            // *** SỬA LỖI: Thêm kiểm tra nếu danh sách `bikes` rỗng ***
            if (bikes.isEmpty()) {
                System.out.println(">>> No bikes found to seed maintenances.");
                return;
            }

            List<Maintenance> maintenances = new ArrayList<>();
            int bikeCount = bikes.size(); // Lấy kích thước danh sách một lần

            // *** SỬA LỖI: `orElse` trỏ về index 0 an toàn ***
            // Lấy xe có status Maintenance
            Bike bike1 = bikes.stream()
                    .filter(b -> b.getBikeStatus() == Bike.BikeStatus.Maintenance)
                    .findFirst()
                    .orElse(bikes.get(0)); // An toàn, dùng index 0
            maintenances.add(createMaintenance(bike1, LocalDate.now().minusDays(1), "Bảo trì phanh, xích", 150000));

            // *** SỬA LỖI: Dùng `random.nextInt(bikeCount)` để đảm bảo index an toàn ***
            // Lấy xe khác
            Bike bike2 = bikes.get(random.nextInt(bikeCount)); // An toàn, dùng index ngẫu nhiên
            maintenances.add(createMaintenance(bike2, LocalDate.now().plusDays(2), "Thay lốp", 250000));

            // Thêm 18 bản ghi bảo trì ngẫu nhiên
            for (int i = 0; i < 18; i++) {
                // *** SỬA LỖI: Dùng `random.nextInt(bikeCount)` ***
                Bike randomBike = bikes.get(random.nextInt(bikeCount));
                LocalDate randomDate = LocalDate.now().minusDays(random.nextInt(30)); // Ngày trong quá khứ
                double randomCost = 50000 + (random.nextInt(5) * 50000); // Chi phí từ 50k - 250k
                maintenances.add(createMaintenance(randomBike, randomDate, "Kiểm tra định kỳ", randomCost));
            }
            maintenanceRepo.saveAll(maintenances);
        }
    }


    private Maintenance createMaintenance(Bike bike, LocalDate date, String desc, double cost) {
        Maintenance m = new Maintenance();
        m.setBikeId(bike);
        m.setMaintenanceDate(date);
        m.setMaintenanceDesc(desc);
        m.setMaintenanceCost(cost);
        m.setMaintenanceStatus(MaintenanceStatus.ABLE);
        return m;
    }

    // Phương thức phức tạp nhất: tạo đơn thuê và thanh toán liên quan
    private void seedRentalOrdersAndPayments() {
        if (rentalOrderRepository.count() == 0 && customerRepo.count() > 0 && bikeRepository.count() > 0) {
            System.out.println(">>> Seeding rental orders and payments...");

            List<Customer> customers = customerRepo.findAll();
            List<Bike> allBikes = bikeRepository.findAll(); // Lấy tất cả xe

            // *** SỬA LỖI: Đảm bảo có đủ xe để tạo đơn, nếu không sẽ bị lỗi ở 'availableBikes.get(availableBikeIndex++)' ***
            List<Bike> availableBikes = allBikes.stream()
                    .filter(b -> b.getBikeStatus() == Bike.BikeStatus.Available).toList();

            List<Promotion> promotions = promotionRepo.findAll().stream()
                    .filter(p -> p.getPromotionStatus() == PromotionStatus.ABLE && p.getPromotionEndDate().isAfter(LocalDate.now())).toList();

            List<RentalOrder> ordersToSave = new ArrayList<>();
            List<Payment> paymentsToSave = new ArrayList<>();
            List<Bike> bikesToUpdate = new ArrayList<>(); // Để cập nhật trạng thái xe

            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            int availableBikeIndex = 0;

            for (int i = 0; i < 20; i++) {
                // *** SỬA LỖI: Dừng lại nếu hết xe có sẵn (availableBikes) ***
                if (availableBikeIndex >= availableBikes.size()) {
                    System.out.println(">>> Ran out of available bikes to seed rental orders.");
                    break;
                }

                Customer randomCustomer = customers.get(random.nextInt(customers.size()));
                Bike bike = availableBikes.get(availableBikeIndex++); // Lấy xe và tăng index
                Promotion randomPromotion = (promotions.size() > 0 && random.nextBoolean()) ? promotions.get(random.nextInt(promotions.size())) : null;

                long daysAgo = random.nextInt(60); // 0-59 ngày trước
                LocalDate rentalDate = today.minusDays(daysAgo);

                // Đảm bảo thời gian thuê hợp lệ
                LocalTime rentalTime;
                if (daysAgo > 0) {
                    rentalTime = LocalTime.of(random.nextInt(23), random.nextInt(59)); // Thời gian ngẫu nhiên trong ngày quá khứ
                } else {
                    // Nếu là hôm nay, thời gian phải trước 'now'
                    int hour = random.nextInt(now.getHour() + 1);
                    int minute = (hour == now.getHour()) ? random.nextInt(now.getMinute() + 1) : random.nextInt(59);
                    rentalTime = LocalTime.of(hour, minute);
                }


                double totalAmount = bike.getBikeRentPerHour() * (random.nextInt(5) + 1); // Giả lập thuê 1-5 giờ
                if (randomPromotion != null) {
                    if (randomPromotion.getPromotionType() == PromotionType.PERCENTAGE) {
                        totalAmount = totalAmount - (totalAmount * randomPromotion.getPromotionDiscount() / 100);
                    } else {
                        totalAmount = totalAmount - randomPromotion.getPromotionDiscount();
                    }
                    if (totalAmount < 0) totalAmount = 0;
                }

                RentalOrder.RentalStatus status;
                if (i % 3 == 0) { // Cứ 3 đơn thì 1 đơn đang "ONGOING"
                    status = RentalOrder.RentalStatus.ONGOING;
                    bike.setBikeStatus(Bike.BikeStatus.Unavailable); // Cập nhật trạng thái xe
                    bikesToUpdate.add(bike);
                    rentalDate = today; // Đơn đang thuê thì là hôm nay

                    int hour = random.nextInt(now.getHour() + 1);
                    int minute = (hour == now.getHour()) ? random.nextInt(now.getMinute() + 1) : random.nextInt(59);
                    rentalTime = LocalTime.of(hour, minute); // Bắt đầu 0-n giờ trước

                } else if (i % 10 == 0) { // 1/10 đơn bị "CANCELLED"
                    status = RentalOrder.RentalStatus.CANCELLED;
                    // Xe không bị ảnh hưởng, không cần add vào bikesToUpdate
                    availableBikeIndex--; // Trả lại xe vào pool
                } else {
                    status = RentalOrder.RentalStatus.COMPLETED;
                    // Xe đã trả, trạng thái nên là Available (nhưng nó đã là Available từ đầu rồi, nên ta không cần đổi)
                    availableBikeIndex--; // Trả lại xe vào pool
                }

                RentalOrder order = createOrder(randomCustomer, bike, randomPromotion, rentalDate, rentalTime, status, totalAmount);
                ordersToSave.add(order);
            }

            // Lưu đơn hàng trước để lấy ID
            rentalOrderRepository.saveAll(ordersToSave);
            // Cập nhật trạng thái xe (chỉ những xe chuyển sang Unavailable)
            bikeRepository.saveAll(bikesToUpdate);

            // Tạo thanh toán cho các đơn "COMPLETED"
            for (RentalOrder order : ordersToSave) {
                if (order.getRentalOrderStatus() == RentalOrder.RentalStatus.COMPLETED) {
                    Payment.PaymentMethod method = random.nextBoolean() ? Payment.PaymentMethod.CASH : Payment.PaymentMethod.TRANSFER;
                    Payment payment = createPayment(order, method, order.getRentalOrderTotalAmount(), order.getRentalOrderRentalDate());
                    paymentsToSave.add(payment);
                }
            }
            paymentRepository.saveAll(paymentsToSave);
        }
    }


    private RentalOrder createOrder(Customer c, Bike b, Promotion p, LocalDate date, LocalTime time, RentalOrder.RentalStatus status, double total) {
        RentalOrder ro = new RentalOrder();
        ro.setCustomerId(c);
        ro.setBikeId(b);
        ro.setPromotionId(p);
        ro.setRentalOrderRentalDate(date);
        ro.setRentalOrderRentalTime(time);
        ro.setRentalOrderStatus(status);
        ro.setRentalOrderTotalAmount(total);
        ro.setRentalOrderActiveStatus(RentalOrder.ActiveStatus.ABLE);
        return ro;
    }

    private Payment createPayment(RentalOrder ro, Payment.PaymentMethod method, double amount, LocalDate date) {
        Payment p = new Payment();
        p.setRentalOrder(ro);
        p.setPaymentMethod(method);
        p.setPaymentAmount(amount);
        p.setPaymentDate(date.plusDays(random.nextInt(2))); // Thanh toán trong vòng 2 ngày sau khi thuê
        p.setPaymentStatus(Payment.PaymentStatus.Able);
        return p;
    }
}