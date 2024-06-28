package com.momo.momopjt.club;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ModelMapper modelMapper;
    private final ClubRepository clubRepository;

    @Override
    public Long createClub(ClubDTO clubDTO) {
        Club club = modelMapper.map(clubDTO, Club.class);
        Long clubNo = clubRepository.save(club).getClubNo();
        return clubNo;
    }

    @Override
    @Transactional
    public ClubDTO readOneClub(Long clubNo) {
        Optional<Club> result = clubRepository.findById(clubNo);

        Club club = result.orElseThrow();

        ClubDTO clubDTO = modelMapper.map(club, ClubDTO.class);

        return clubDTO;
    }

    @Override
    public List<ClubDTO> readAllClub() {
        List<Club> clubs = clubRepository.findAll();
        List<ClubDTO> clubDTOS = modelMapper.map(clubs, List.class);
        return clubDTOS;
    }

}