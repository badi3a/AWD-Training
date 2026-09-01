<?php
declare(strict_types=1); namespace App\Repository; use App\Entity\Candidate; use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository; use Doctrine\Persistence\ManagerRegistry;
final class CandidateRepository extends ServiceEntityRepository { public function __construct(ManagerRegistry $r){parent::__construct($r,Candidate::class);} }
