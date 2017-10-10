package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Presentation;
import com.mycompany.myapp.domain.Room;
import com.mycompany.myapp.repository.PresentationRepository;
import com.mycompany.myapp.repository.RoomRepository;
import com.mycompany.myapp.repository.ScheduleRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.dto.RoomDTO;
import com.mycompany.myapp.service.dto.ScheduleDTO;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Room.
 */
@RestController
@RequestMapping("/api")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);

    private static final String ENTITY_NAME = "room";

    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
    private final PresentationRepository presentationRepository;

    public RoomResource(RoomRepository roomRepository, ScheduleRepository scheduleRepository, PresentationRepository presentationRepository) {
        this.roomRepository = roomRepository;
        this.scheduleRepository = scheduleRepository;
        this.presentationRepository = presentationRepository;
    }

    /**
     * POST  /rooms : Create a new room.
     *
     * @param room the room to create
     * @return the ResponseEntity with status 201 (Created) and with body the new room, or with status 400 (Bad Request) if the room has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rooms")
    @Secured(AuthoritiesConstants.PRESENTER)
    @Timed
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) throws URISyntaxException {
        log.debug("REST request to save Room : {}", room);
        if (room.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new room cannot already have an ID")).body(null);
        }
        Room result = roomRepository.save(room);
        return ResponseEntity.created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rooms : Updates an existing room.
     *
     * @param room the room to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated room,
     * or with status 400 (Bad Request) if the room is not valid,
     * or with status 500 (Internal Server Error) if the room couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rooms")
    @Secured(AuthoritiesConstants.PRESENTER)
    @Timed
    public ResponseEntity<Room> updateRoom(@Valid @RequestBody Room room) throws URISyntaxException {
        log.debug("REST request to update Room : {}", room);
        if (room.getId() == null) {
            return createRoom(room);
        }
        Room result = roomRepository.save(room);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, room.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rooms : get all the rooms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rooms in body
     */
    @GetMapping("/rooms")
    @Timed
    public List<Room> getAllRooms() {
        log.debug("REST request to get all Rooms");
        return  roomRepository.findAll();
    }

    @GetMapping("/rooms/presentations")
    @Timed
    public List<RoomDTO> getAllRoomsWithPresentations() {
        log.debug("REST request to get all Rooms");

        return roomRepository.findAll().stream().map(room -> new RoomDTO(room)).collect(Collectors.toList());
    }
    /**
     * GET  /rooms/:id : get the "id" room.
     *
     * @param id the id of the room to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the room, or with status 404 (Not Found)
     */
    @GetMapping("/rooms/{id}")
    @Timed
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        Room room = roomRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(room));
    }

    /**
     * DELETE  /rooms/:id : delete the "id" room.
     *
     * @param id the id of the room to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rooms/{id}")
    @Secured(AuthoritiesConstants.PRESENTER)
    @Timed
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
