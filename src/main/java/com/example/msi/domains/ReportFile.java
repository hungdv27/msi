package com.example.msi.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "report_file")
public class ReportFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "report_id", nullable = false)
  private int reportId;

  @Column(name = "file_id", nullable = false)
  private int fileId;
}
