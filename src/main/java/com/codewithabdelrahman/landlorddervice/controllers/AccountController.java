package com.codewithabdelrahman.landlorddervice.controllers;

import com.codewithabdelrahman.landlorddervice.models.*;
import com.codewithabdelrahman.landlorddervice.repositories.AppUserRepository;
import com.codewithabdelrahman.landlorddervice.repositories.PostRepository;
import com.codewithabdelrahman.landlorddervice.services.EmailService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;
    @Value("${security.jwt.issuer}")
    private String jwtIssuer;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    private MongoOperations postRepositoryMongo;

    private Map<String, String> verificationCodes = new HashMap<>();

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterDto registerDto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            Map<String, String> errorsMap = new HashMap<>();
            for (Object errorObj : result.getAllErrors()) {
                var error = (FieldError) errorObj;
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            response.put("status", false);
            response.put("errors", errorsMap);
            return ResponseEntity.badRequest().body(response);
        }
        var bCryptEncoder = new BCryptPasswordEncoder();
        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setEmail(registerDto.getEmail());
        appUser.setPhone(registerDto.getPhone());
        appUser.setRole("client");
        appUser.setCreatedAt(new Date());
        appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
        appUser.setProfileImage("https://student.valuxapps.com/storage/assets/defaults/user.jpg");
        try {
            if (appUserRepository.findByUsername(registerDto.getUsername()) != null ||
                    appUserRepository.findByEmail(registerDto.getEmail()) != null) {
                response.put("status", false);
                response.put("message", "You Enter Bad Data.");
                return ResponseEntity.badRequest().body(response);
            }
            appUser = appUserRepository.save(appUser);
            String jwtToken = createJwtToken(appUser);
            appUser.setToken(jwtToken);
            appUserRepository.save(appUser);
            response.put("status", true);
            response.put("message", "You signed up successfully.");
            response.put("user", appUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "An error occurred during registration.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginDto loginDto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            Map<String, String> errorsMap = new HashMap<>();
            for (Object errorObj : result.getAllErrors()) {
                var error = (FieldError) errorObj;
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            response.put("status", false);
            response.put("errors", errorsMap);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            AppUser appUser = appUserRepository.findByEmail(loginDto.getEmail());
            if (appUser == null) {
                response.put("status", false);
                response.put("message", "Invalid email or password.");
                return ResponseEntity.badRequest().body(response);
            }
            String jwtToken = createJwtToken(appUser);
            appUser.setToken(jwtToken);
            appUserRepository.save(appUser);
            response.put("status", true);
            response.put("user", appUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            response.put("status", false);
            response.put("message", "Invalid email or password.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(
            @RequestBody ForgotPasswordDto forgotPasswordDto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("status", false);
            response.put("message", "Invalid email format.");
            return ResponseEntity.badRequest().body(response);
        }
        Optional<AppUser> userOptional = Optional.ofNullable(appUserRepository.findByEmail(forgotPasswordDto.getEmail()));
        if (userOptional.isEmpty()) {
            response.put("status", false);
            response.put("message", "User not found.");
            return ResponseEntity.badRequest().body(response);
        }
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(forgotPasswordDto.getEmail(), verificationCode);
        emailService.sendEmail(
                forgotPasswordDto.getEmail(),
                "Password Reset Code",
                "Your password reset code is: " + verificationCode
        );
        response.put("status", true);
        response.put("message", "Verification code sent to email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");
        String code = request.get("code");
        String newPassword = request.get("newPassword");
        if (!verificationCodes.containsKey(email) || !verificationCodes.get(email).equals(code)) {
            response.put("status", false);
            response.put("message", "Invalid verification code.");
            return ResponseEntity.badRequest().body(response);
        }
        Optional<AppUser> userOptional = Optional.ofNullable(appUserRepository.findByEmail(email));
        if (userOptional.isEmpty()) {
            response.put("status", false);
            response.put("message", "User not found.");
            return ResponseEntity.badRequest().body(response);
        }
        AppUser user = userOptional.get();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        appUserRepository.save(user);
        verificationCodes.remove(email);
        response.put("status", true);
        response.put("message", "Password reset successfully.");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/landlord/update")
    public ResponseEntity<Map<String, Object>> updateUserByToken(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> updates) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var decoder = NimbusJwtDecoder.withSecretKey(new ImmutableSecret<>(jwtSecretKey.getBytes()).getSecretKey())
                    .macAlgorithm(MacAlgorithm.HS256).build();
            var jwt = decoder.decode(token);
            String email = jwt.getClaimAsString("sub");
            AppUser appUser = appUserRepository.findByEmail(email);
            if (appUser == null) {
                response.put("status", false);
                response.put("message", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            if (updates.containsKey("phone")) {
                appUser.setPhone(updates.get("phone"));
            }
            if (updates.containsKey("address")) {
                appUser.setAddress(updates.get("address"));
            }
            if (updates.containsKey("profileImage")) {
                appUser.setProfileImage(updates.get("profileImage"));
            }
            appUserRepository.save(appUser);
            response.put("status", true);
            response.put("message", "User updated successfully.");
            response.put("user", appUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("status", false);
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @DeleteMapping("/delete/landlord")
    public ResponseEntity<Map<String, Object>> deleteUserByToken(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var decoder = NimbusJwtDecoder.withSecretKey(new ImmutableSecret<>(jwtSecretKey.getBytes()).getSecretKey())
                    .macAlgorithm(MacAlgorithm.HS256).build();
            var jwt = decoder.decode(token);
            String email = jwt.getClaimAsString("sub");
            AppUser appUser = appUserRepository.findByEmail(email);
            if (appUser == null) {
                response.put("status", false);
                response.put("message", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            appUserRepository.delete(appUser);
            response.put("status", true);
            response.put("message", "User deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("status", false);
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/landlord")
    public ResponseEntity<Map<String, Object>> getUserByToken(
            @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var decoder = NimbusJwtDecoder.withSecretKey(new ImmutableSecret<>(jwtSecretKey.getBytes()).getSecretKey()).macAlgorithm(MacAlgorithm.HS256).build();
            var jwt = decoder.decode(token);
            String email = jwt.getClaimAsString("sub");
            AppUser appUser = appUserRepository.findByEmail(email);
            if (appUser == null) {
                response.put("status", false);
                response.put("message", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            response.put("status", true);
            response.put("user", appUser);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("status", false);
            response.put("message", "Invalid or expired token.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/post/create")
    public ResponseEntity<Map<String, Object>> createPost(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody PostDto postDto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (result.hasErrors()) {
                Map<String, String> errorsMap = new HashMap<>();
                for (Object errorObj : result.getAllErrors()) {
                    var error = (FieldError) errorObj;
                    errorsMap.put(error.getField(), error.getDefaultMessage());
                }
                response.put("status", false);
                response.put("errors", errorsMap);
                return ResponseEntity.badRequest().body(response);
            }

            // Validate token
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            var decoder = NimbusJwtDecoder.withSecretKey(new ImmutableSecret<>(jwtSecretKey.getBytes()).getSecretKey())
                    .macAlgorithm(MacAlgorithm.HS256).build();
            var jwt = decoder.decode(token);
            String email = jwt.getClaimAsString("sub");
            AppUser appUser = appUserRepository.findByEmail(email);
            if (appUser == null) {
                response.put("status", false);
                response.put("message", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Create post - only set fields that are provided
            Post post = new Post();
            post.setUserId(appUser.getId());

            // Required fields
            post.setArea(postDto.getArea());
            post.setNumberOfRooms(postDto.getNumberOfRooms());
            post.setNumberOfBathrooms(postDto.getNumberOfBathrooms());
            post.setRent(postDto.getRent());
            post.setNotes(postDto.getNotes());
            post.setImages(postDto.getImages());

            // Optional fields (only set if they're not null)
            if (postDto.getNeighborhood() != null) post.setNeighborhood(postDto.getNeighborhood());
            if (postDto.getStreet() != null) post.setStreet(postDto.getStreet());
            if (postDto.getBuildingNumber() != null) post.setBuildingNumber(postDto.getBuildingNumber());
            if (postDto.getApartmentNumber() != null) post.setApartmentNumber(postDto.getApartmentNumber());
            if (postDto.isHasInternet() != null) post.setHasInternet(postDto.isHasInternet());
            if (postDto.isHasNaturalGas() != null) post.setHasNaturalGas(postDto.isHasNaturalGas());
            if (postDto.getNumberOfBeds() != null) post.setNumberOfBeds(postDto.getNumberOfBeds());
            if (postDto.isFavorite() != null) post.setFavorite(postDto.isFavorite());
            if (postDto.getFloorNumber() != null) post.setFloorNumber(postDto.getFloorNumber());
            if (postDto.isHasElevator() != null) post.setHasElevator(postDto.isHasElevator());
            if (postDto.getNearbyServices() != null) post.setNearbyServices(postDto.getNearbyServices());

            post.setCreatedAt(new Date());

            // Save post
            post = postRepository.save(post);
            response.put("status", true);
            response.put("message", "Post created successfully.");
            response.put("post", post);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("status", false);
            response.put("message", "An error occurred while creating the post.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/post/all")
    public ResponseEntity<Map<String, Object>> getAllPosts() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Retrieve all posts
            List<Post> posts = postRepository.findAll();

            // Check if posts exist
            if (posts.isEmpty()) {
                response.put("status", false);
                response.put("message", "No posts found.");
                return ResponseEntity.ok(response);
            }

            // Success response
            response.put("status", true);
            response.put("message", "Posts retrieved successfully.");
            response.put("posts", posts);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.put("status", false);
            response.put("message", "An error occurred while retrieving posts.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String createJwtToken(AppUser appUser) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(7 * 24 * 3600))
                .subject(appUser.getEmail())
                .claim("role", appUser.getRole())
                .build();
        var encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        String token = encoder.encode(params).getTokenValue();
        appUser.setToken(token);
        appUserRepository.save(appUser);
        return token;
    }
}