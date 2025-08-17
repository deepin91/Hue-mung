package pethotel.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ConsultingDto {
	private int consultingIdx;
	private String consultingTitle;
	private String contents;
	private String reply;
	private LocalDateTime consultingCreatedt;
	private String cUserId;
}
