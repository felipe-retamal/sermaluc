package cl.sermaluc.gestion.model;

import java.util.ArrayList;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user", 
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name"),
		@UniqueConstraint(columnNames = "email") 
})
public class User {
	
	public User() {
	}

	public User(String name, String email, String password, ArrayList<Phone> phone) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank
	@Size(max = 20)
	private String name;

	@NotBlank
	@Size(max = 50)
	@Email(regexp = ".+[@].+[\\.].+")
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	private String token;
	
	@OneToMany(cascade = CascadeType.ALL)
	private ArrayList<Phone> phone;
	
	//>> encapsulation
	
	public String getToken() {
		return token;
	}

	public ArrayList<Phone> getPhone() {
		return phone;
	}

	public void setPhone(ArrayList<Phone> phone) {
		this.phone = phone;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
