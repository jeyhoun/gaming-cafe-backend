package az.gaming_cafe.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Arrays;

@Entity
@Table(name = PricingEntity.TABLE_NAME)
public class PricingEntity extends BaseAuditEntity{

    public static final String TABLE_NAME = "prices";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    private LocalTime startTime;   // 09:00

    private LocalTime endTime;     // 18:00

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "days_of_week", columnDefinition = "int[]")
    private Integer[] daysOfWeek;//[1,2,3,4,5] Mon-Fri

    private Boolean isActive;

    public PricingEntity() {
    }

    @PrePersist
    public void prePersist() {
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Integer[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PricingEntity)) {
            return false;
        }
        PricingEntity that = (PricingEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PricingEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerHour=" + pricePerHour +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", daysOfWeek=" + Arrays.toString(daysOfWeek) +
                ", isActive=" + isActive +
                '}';
    }
}
