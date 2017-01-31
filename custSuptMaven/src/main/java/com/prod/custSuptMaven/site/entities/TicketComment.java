package com.prod.custSuptMaven.site.entities;
/* class notes- similar to entities/Ticket.Java this was introduced in chap 24 to elimiante need for orig site/TicketComment class
 * DTO function.  very similar changes- replaced /site/entities/TicketCommentEntity and site/TicketComment, so they fit within a 
 * more normal entity structure but allows Instant data type and array of large attachments 
 * In part introducing lazy loading, so not to affect app performance.  Chap 24 also allows attachmetns to be added to comments
 * and its related DB table changes.
 * 
 */
import java.io.Serializable;

//add annotations
public class TicketComment implements Serializable {
	private static final long serialVersionUID = 1L;
	//add guts

}
