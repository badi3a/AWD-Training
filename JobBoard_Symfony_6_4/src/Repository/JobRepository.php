<?php
declare(strict_types=1); namespace App\Repository; use App\Entity\Job; use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository; use Doctrine\Persistence\ManagerRegistry;
final class JobRepository extends ServiceEntityRepository { public function __construct(ManagerRegistry $r){parent::__construct($r,Job::class);}
 public function findPublished(string $query=''):array{ $qb=$this->createQueryBuilder('j')->andWhere('j.published = :yes')->setParameter('yes',true)->orderBy('j.createdAt','DESC'); if($query!=='')$qb->andWhere('LOWER(j.title) LIKE :q OR LOWER(j.description) LIKE :q')->setParameter('q','%'.strtolower($query).'%'); return $qb->getQuery()->getResult(); }
 }
