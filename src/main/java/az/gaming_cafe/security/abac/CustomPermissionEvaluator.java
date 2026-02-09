package az.gaming_cafe.security.abac;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
public class CustomPermissionEvaluator {


//    public boolean canCancelReservation(Authentication auth, Reservation reservation) {
//        String username = auth.getName();
//
//        boolean isOwner = reservation.getUser().getUsername().equals(username);
//
//        boolean isAdmin = auth.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
//
//        return isOwner || isAdmin;
//    }
}
