package com.zzc.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Administrator
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShopChange extends AbstractEvent {
    private static final long serialVersionUID = 3594699690987780228L;

    private String shopId;
    private String shopSceneCode;
    private String province;
    private String city;

    @Override
    public String getIdentifier() {
        return shopId;
    }
}
