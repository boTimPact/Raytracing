# Raytracing
_English version below_

## Dieses Projekt ist im Zuge des Kurses GTAT1 Game Technology & Interactive Systems: Ray Tracing entstanden

Im Praxisteil der Veranstaltung musste der gelernte Stoff in einem eigenen Raytracer implementiert werden.

Behandelte Themen:
- Bilderzeugung durch Strahlverfolgung
- Grundlegende Kamerafunktion
- Lichtquellen
- Materialeigenschaften
- Mathematische Darstellung von Körpern: Kugeln / Quadriken & Transformationen (Translation, Rotation, Skalierung)
- Constructive Solid Geometry (Vereinigung, Schnitt, Differenz)
- Beleuchtungsmodell mit Cook-Torrance
- Schatten-, Reflektions- & Refraktionsstrahlen
- Transparenz & Brechungsindex
- Pathtracing (hier für weiche Schatten)
<br>
<br>


_English version_
## This project was created as part of the GTAT1 Game Technology & Interactive Systems: Ray Tracing course.

In the practical part of the course, the material learned had to be implemented in an own ray tracer.

Covered Topics:
- Image generation through ray tracing
- Basic camera function
- Light sources
- Material properties
- Mathematical representation of solids: spheres / quadrics & transformations (translation, rotation, scaling)
- Constructive Solid Geometry(CSG) - union, intersection, difference
- Illumination model with Cook-Torrance
- Shadow, reflection & refraction rays
- Transparency & Refractive Index
- Pathtracing (here for soft shadows)

## Results:
### 1. Iteration
Simple raytracer for rendering spheres with diffuse illumination
<picture>
  <source srcset="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue1.png?raw=true">
  <img alt="Basic Raytracer render showing 3 spheres with diffuse lighting" src="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue1.png?raw=true">
</picture>

### 2. Iteration
Cook-Torrance lighting model with quadrics and CSGs
<picture>
  <source srcset="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue2.png?raw=true">
  <img alt="Raytraced render showing Quadrics and Constructive Solid Geometry and the Cook-Torrance ilumination model" src="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue2.png?raw=true">
</picture>

### 3. Iteration
Shadows, reflections, transparency and refractions
<picture>
  <source srcset="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue3_P1.png?raw=true">
  <img alt="Raytraced render showcasing reflections and refractions with lightsource shining directly onto Objects" src="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue3_P1.png?raw=true">
</picture>
<picture>
  <source srcset="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue3_P2.png?raw=true">
  <img alt="Raytraced render showcasing reflections and refractions with lightsource behind Objects"      src="https://github.com/boTimPact/Raytracing/blob/master/Pictures/Raytracing_Ue3_P2.png?raw=true">
</picture>

### 4. Iteration
Path tracing for soft shadows, ++

_Work in Progress_
