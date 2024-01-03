package org.nbc.account.trollo.domain.invitation.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.nbc.account.trollo.domain.board.entity.Board;
import org.nbc.account.trollo.domain.board.repository.BoardRepository;
import org.nbc.account.trollo.domain.invitation.dto.response.InvitationsRes;
import org.nbc.account.trollo.domain.invitation.dto.response.ReceivedInvitationRes;
import org.nbc.account.trollo.domain.invitation.dto.response.SentInvitationRes;
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

        List<ReceivedInvitationRes> receivedInvitations = receivedInvitationList
            .stream()
            .map((Invitation invitation) -> new ReceivedInvitationRes(
                invitation.getId().getBoard().getId(),
                invitation.getId().getBoard().getName(),
                invitation.getSender().getId(),
                invitation.getSender().getNickname()
                )
            ).toList();

        List<Invitation> sentInvitationList = invitationRepository.findAllBySender(user)
            .orElseThrow(() ->
                new InvitationDomainException(ErrorCode.NOT_FOUND_INVITATION));

        List<SentInvitationRes> sentInvitations = sentInvitationList
            .stream()
            .map((Invitation invitation) -> new SentInvitationRes(
                    invitation.getId().getBoard().getId(),
                    invitation.getId().getBoard().getName(),
                    invitation.getId().getReceiver().getId(),
                    invitation.getId().getReceiver().getNickname()
                )
            ).toList();

        return new InvitationsRes(receivedInvitations, sentInvitations);
    }

    @Transactional
    @Override
    public void approveInvitation(Long boardId, User user) {

        Board board = getBoardById(boardId);

        Invitation invitation = getInvitation(user, board);

        //delete existing invitation
        invitationRepository.delete(invitation);

        //save new User and Board relation which means the person is a participant of the board
        UserBoard newUserBoard = new UserBoard(user, board, UserBoardRole.PARTICIPANT);
        userBoardRepository.save(newUserBoard);

    }

    @Transactional
    @Override
    public void rejectInvitation(Long boardId, User user) {

        Board board = getBoardById(boardId);
        Invitation invitation = getInvitation(user, board);
        invitationRepository.delete(invitation);

    }

    @Override
    public void cancelInvitation(Long boardId, Long userId, User user) {

        Board board = getBoardById(boardId);
        User receiver = getUserById(userId);
        Invitation invitation = getInvitation(receiver, board);

        //only the person who sent the invitation can cancel the invitation
        if(!invitation.getSender().getId().equals(user.getId())){
            throw new InvitationDomainException(ErrorCode.ONLY_SENDER_CAN_CANCEL_INVITATION);
        }

        invitationRepository.delete(invitation);

    }

    private User getUserById(Long userId){

        User user = userRepository.findById(userId).orElseThrow(() ->
            new InvitationDomainException(ErrorCode.NOT_FOUND_USER));

        return user;
    }

    private User getReceiverById(Long userId, User user) {

        User receiver = getUserById(userId);

        //can't invite oneself
        if (receiver.getId().equals(user.getId())) {
            throw new InvitationDomainException(ErrorCode.SELF_CANNOT_BE_INVITED);
        }

        return receiver;

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