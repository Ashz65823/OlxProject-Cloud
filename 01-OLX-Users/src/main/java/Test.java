import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

	public static void main(String[] args) {
		BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
		System.out.println(encoder.encode("Savitha123"));
	}

	//$2a$10$x/6TSfBe2atQQBqOk.vULeyjdyme/aXqhEZDWtqbEhB8uzDGnY8YG  --Aishu12
	//$2a$10$YcfDrawgBtxAooVqBH8qpum0Bm1lpJ5s5M1XHDB6B0HswaWpdEqOq --12345
	//$2a$10$Hg2TqzZtptmin50deEBCSuFMzsbsusw7eEhYpdbsaMc8C4trhZlLu --12345
}
