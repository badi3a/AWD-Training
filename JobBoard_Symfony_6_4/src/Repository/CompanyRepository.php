<?php
declare(strict_types=1); namespace App\Repository; use App\Entity\Company; use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository; use Doctrine\Persistence\ManagerRegistry;
final class CompanyRepository extends ServiceEntityRepository { public function __construct(ManagerRegistry $r){parent::__construct($r,Company::class);} }
