package org.nbc.account.trollo.domain.invitation.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.invitation.dto.response.InvitationRes;
import org.nbc.account.trollo.domain.invitation.dto.response.InvitationsRes;
import org.nbc.account.trollo.domain.invitation.dto.response.UserBoardRes;
import org.nbc.account.trollo.domain.invitation.entity.Invitation;
import org.nbc.account.trollo.domain.invitation.exception.InvitationDomainException;
import org.nbc.account.trollo.domain.invitation.repository.InvitationRepository;
import org.nbc.account.trollo.domain.invitation.service.InvitationService;
import org.nbc.account.trollo.domain.user.entity.User;
import org.nbc.account.trollo.domain.user.repository.UserRepository;
import org.nbc.account.trollo.domain.userboard.entity.UserBoard;
import org.nbc.account.trollo.domain.userboard.entity.UserBoardRole;
import org.nbc.account.trollo.domain.userboard.repository.UserBoardRepository;
import org.nbc.account.trollo.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;

    @Override
    public void createInvitation(Long boardId, Long userId, User user) {

        Board board = getBoardById(boardId);
        checkIfParticipants(user, board);

        //check if the person is inviting oneself
        User receiver = getReceiverById(userId, user);

        //check if invitation exists with the boardId and userId
        checkIfInvitationExists(receiver, board);

        Invitation invitation = Invitation.builder()
            .receiver(receiver)
            .board(board)
            .sender(user)
            .build();
        invitationRepository.save(invitation);

    }

    @Override
    public InvitationsRes getInvitations(User user) {

        List<Invitation> receivedInvitationList = invitationRepository.findAllByIdReceiver(user)
            .orElseThrow(() ->
                new InvitationDomainException(ErrorCode.NOT_FOUND_INVITATION)
            );

        List<InvitationRes> receivedInvitations = receivedInvitationList.stream()
            .map((Invitation invitation) -> new InvitationRes(
                invitation.getId().getBoard().getId())).toList();

        List<Invitation> sentInvitationList = invitationRepository.findAllBySender(user)
            .orElseThrow(() ->
                new InvitationDomainException(ErrorCode.NOT_FOUND_INVITATION));

        List<InvitationRes> sentInvitations = sentInvitationList.stream()
            .map((Invitation invitation) -> new InvitationRes(
                invitation.getId().getBoard().getId())).toList();

        return new InvitationsRes(receivedInvitations, sentInvitations);
    }

    @Transactional
    @Override
    public UserBoardRes approveInvitation(Long boardId, User user) {

        Board board = getBoardById(boardId);

        Invitation invitation = getInvitation(user, board);

        //delete existing invitation
        invitationRepository.delete(invitation);

        //save new User and Board relation which means the person is a participant of the board
        UserBoard newUserBoard = new UserBoard(user, board, UserBoardRole.PARTICIPANT);
        userBoardRepository.save(newUserBoard);

        return new UserBoardRes(newUserBoard.getUser().getEmail(), newUserBoard.getBoard().getId(),
            newUserBoard.getRole());

    }

    @Transactional
    @Override
    public void rejectInvitation(Long boardId, User user) {

        Board board = getBoardById(boardId);
        Invitation invitation = getInvitation(user, board);
        invitationRepository.delete(invitation);

    }

    private User getReceiverById(Long userId, User user) {

        User guest = userRepository.findById(userId).orElseThrow(() ->
            new InvitationDomainException(ErrorCode.NOT_FOUND_USER));

        //can't invite oneself
        if (guest.getId().equals(user.getId())) {
            throw new InvitationDomainException(ErrorCode.SELF_CANNOT_BE_INVITED);
        }

        return guest;

    }

    private Board getBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
            new InvitationDomainException(ErrorCode.NOT_FOUND_BOARD));

        return board;
    }

    private void checkIfInvitationExists(User receiver, Board board) {

        if (invitationRepository.existsByIdReceiverAndIdBoard(receiver, board)) {
            throw new InvitationDomainException(ErrorCode.ALREADY_EXIST_INVITATION);
        }

    }

    private void checkIfParticipants(User user, Board board) {
        UserBoard userBoard = userBoardRepository.findUserBoardByUserAndBoard(user, board)
            .orElseThrow(() ->
                new InvitationDomainException(ErrorCode.ONLY_PARTICIPANTS_CAN_INVITE));
    }

    private Invitation getInvitation(User receiver, Board board) {
        Invitation invitation = invitationRepository.findInvitationByIdReceiverAndIdBoard(receiver,
                board)
            .orElseThrow(() -> new InvitationDomainException(ErrorCode.NOT_FOUND_INVITATION));

        return invitation;
    }

}