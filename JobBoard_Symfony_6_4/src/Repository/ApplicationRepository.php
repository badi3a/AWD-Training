<?php
declare(strict_types=1); namespace App\Repository; use App\Entity\Application; use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository; use Doctrine\Persistence\ManagerRegistry;
final class ApplicationRepository extends ServiceEntityRepository { public function __construct(ManagerRegistry $r){parent::__construct($r,Application::class);} }
