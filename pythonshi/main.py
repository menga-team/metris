import math
import threading
import time
from collections import Counter
from math import sqrt
from random import shuffle
from statistics import median
from threading import Thread

import pygame
import random

from pygame import Color, SurfaceType

# Initialize pygame
pygame.init()
font = pygame.font.Font(None, 36)

# Constants
BLOCK_SIZE = 50
COLUMNS, ROWS = 10, 20
WIDTH, HEIGHT = BLOCK_SIZE*COLUMNS, BLOCK_SIZE*ROWS
(WHITE, BLACK, GRAY) = (255, 255, 255), (0, 0, 0), (128, 128, 128)
(RED, GREEN, BLUE, YELLOW, CYAN, ORANGE, PURPLE) = (255, 0, 0), (0, 255, 0), (0, 0, 255), (255, 255, 0), (0, 255, 255), (255, 128, 0), (255, 0, 255)

# Shapes
SHAPES = [
    [[
        [0, 0, 0, 0],
        [1, 1, 1, 1],
        [0, 0, 0, 0],
        [0, 0, 0, 0],
            ],[
        [0, 1, 0, 0],
        [0, 1, 0, 0],
        [0, 1, 0, 0],
        [0, 1, 0, 0],
      ]],  # I

    [[
        [1, 0, 0],
        [1, 1, 1],
        [0, 0, 0],
            ],[
        [0, 1, 1],
        [0, 1, 0],
        [0, 1, 0],
            ],[
        [0, 0, 0],
        [1, 1, 1],
        [0, 0, 1],
            ],[
        [0, 1, 0],
        [0, 1, 0],
        [1, 1, 0],
    ]],  # J
    [[
        [0, 0, 1],
        [1, 1, 1],
        [0, 0, 0],
            ],[
        [0, 1, 0],
        [0, 1, 0],
        [0, 1, 1],
            ],[
        [0, 0, 0],
        [1, 1, 1],
        [1, 0, 0],
            ],[
        [1, 1, 0],
        [0, 1, 0],
        [0, 1, 0],
    ]],  # L
    [[
        [1, 1],
        [1, 1]
    ]],  # O

    [[
        [0, 1, 1],
        [1, 1, 0],
        [0, 0, 0],
            ],[
        [0, 1, 0],
        [0, 1, 1],
        [0, 0, 1],
            ],[
        [0, 0, 0],
        [0, 1, 1],
        [1, 1, 0],
            ],[
        [1, 0, 0],
        [1, 1, 0],
        [0, 1, 0],
    ]],  # S

    [[
        [0, 1, 0],
        [1, 1, 1],
        [0, 0, 0],
    ],[
        [0, 1, 0],
        [0, 1, 1],
        [0, 1, 0],
    ],[
        [0, 0, 0],
        [1, 1, 1],
        [0, 1, 0],
    ],[
        [0, 1, 0],
        [1, 1, 0],
        [0, 1, 0],
    ]],  # T
    [[
        [1, 1, 0],
        [0, 1, 1],
        [0, 0, 0],
    ],[
        [0, 0, 1],
        [0, 1, 1],
        [0, 1, 0],
    ],[
        [0, 0, 0],
        [1, 1, 0],
        [0, 1, 1],
    ],[
        [0, 1, 0],
        [1, 1, 0],
        [1, 0, 0],
    ]],  # Z
]


class Tetrimino:
    def __init__(self, shape_index=None, x=None, y=0,rotation=0):
        if shape_index is None:
            self.shape_index = random.randint(0, len(SHAPES)-1)
        else:
            self.shape_index = shape_index
        self.shape = SHAPES[self.shape_index]
        # self.shape = SHAPES[3]
        self.rotate(rotation)
        self.tetromino = self.shape[self.rotation]
        if x is None:
            self.x = self.get_x_origin()
        else:
            self.x = x
        self.y = y
        self.color = [BLUE, GREEN, RED, YELLOW, CYAN, ORANGE, PURPLE][self.shape_index]

    def get_x_origin(self):
        return COLUMNS // 2 - len(self.tetromino[0]) // 2

    def copy(self):
        tetro = Tetrimino()
        tetro.shape = self.shape
        tetro.shape_index = self.shape_index
        tetro.rotate(self.rotation)
        tetro.tetromino = self.tetromino
        tetro.x = self.x
        tetro.y = self.y
        tetro.color = self.color
        return tetro

    def modulo_rotation(self, rot):
        return rot % (len(self.shape))

    def rotate(self, rot):
        self.rotation = self.modulo_rotation(rot)
        self.tetromino = self.shape[self.rotation]


class Tetris:
    def __init__(self, overlay):
        self.grid = [[BLACK for x in range(COLUMNS)] for y in range(ROWS)]
        # self.grid = [[(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (255, 0, 0), (255, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)],
        #              [(0, 0, 0), (255, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0), (0, 0, 0)]]
        # self.grid = [[BLACK if y < 15 or x == y-10 else WHITE for x in range(COLUMNS)] for y in range(ROWS)]
        self.past_pieces = []
        self.current_piece = Tetrimino()
        self.running = True
        self.score = 0
        self.paused = True
        self.last_update = 0
        self.key_pressed = False
        self.solver = Solver(self)
        self.overlay: SurfaceType = overlay

    def replenish_piecepool(self):
        for pool in self.nextpiecepool:
            if len(pool) < len(SHAPES):
                l = [i for i in range(len(SHAPES)) if i not in pool]
                random.shuffle(l)
                pool += l

    def pop_next_piece(self):
        p = Counter([i for i in range(len(SHAPES))] + self.past_pieces[-len(SHAPES)+2:])
        population = list(p.keys())
        weights = [1 / (x ** 2) for n, x in enumerate(p.values())]
        res = random.choices(population=population, weights=weights)[0]
        self.past_pieces.append(res)
        return res

    def draw_grid(self, screen):
        for y, row in enumerate(self.grid):
            for x, color in enumerate(row):
                pygame.draw.rect(screen, color, (x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE))
                pygame.draw.rect(screen, GRAY, (x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE), 1)

    def draw_piece(self, screen):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    pygame.draw.rect(screen, self.current_piece.color,
                                     ((self.current_piece.x + x) * BLOCK_SIZE, (self.current_piece.y + y) * BLOCK_SIZE,
                                      BLOCK_SIZE, BLOCK_SIZE))

    def draw_paths(self, screen):
        # for i in self.solver.all_possible_places:
        #     x = i[0]
        #     y = i[1]
        #     r = i[2]
        #     pygame.draw.rect(screen, RED, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))

        for i in self.solver.past_pos:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, GRAY, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))

        for i in self.solver.possible_placings_pathfound:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, WHITE, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))

        for i in self.solver.piece_path:
            x = i[0]
            y = i[1]
            r = i[2]
            pygame.draw.rect(screen, GREEN, ((x*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r//2) *2), y*BLOCK_SIZE + BLOCK_SIZE//5 + ((BLOCK_SIZE//5) * (r%2) * 2)), (BLOCK_SIZE//5, BLOCK_SIZE//5)))


        x = self.current_piece.x
        y = self.current_piece.y
        r = self.current_piece.rotation
        pygame.draw.rect(screen, WHITE, ((x * BLOCK_SIZE + BLOCK_SIZE // 5 + (
                    (BLOCK_SIZE // 5) * (r // 2) * 2), y * BLOCK_SIZE + BLOCK_SIZE // 5 + (
                                                     (BLOCK_SIZE // 5) * (r % 2) * 2)),
                                        (BLOCK_SIZE // 5, BLOCK_SIZE // 5)))
        # for y, row in enumerate(self.current_piece.tetromino):
        #     for x, cell in enumerate(row):
        #         if cell:
        #             pygame.draw.rect(screen, (self.current_piece.color[0], self.current_piece.color[0], self.current_piece.color[0], 128),
        #                              ((self.current_piece.x + x) * BLOCK_SIZE, (self.current_piece.y + y) * BLOCK_SIZE,
        #                               BLOCK_SIZE, BLOCK_SIZE))

    def draw_placment_preview(self, screen):
        pos = [self.current_piece.x, self.current_piece.y, self.current_piece.rotation]
        try:
            pos = self.solver.possible_placings_pathfound[self.solver.placment_previeing]
        except IndexError:
            return
        finally:
            piece = Tetrimino(self.current_piece.shape_index, pos[0], pos[1], pos[2])
            for y, row in enumerate(piece.tetromino):
                for x, cell in enumerate(row):
                    if cell:
                        pygame.draw.rect(screen,  Color(self.current_piece.color)//Color(4, 4, 4),
                                         ((pos[0] + x) * BLOCK_SIZE, (pos[1] + y) * BLOCK_SIZE,
                                          BLOCK_SIZE, BLOCK_SIZE))



    def check_collision(self, dx=0, dy=0):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    new_x, new_y = self.current_piece.x + x + dx, self.current_piece.y + y + dy
                    if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.grid[new_y][new_x] != BLACK):
                        return True
        return False

    def move_piece(self, dx, dy):
        if not self.check_collision(dx, dy):
            self.current_piece.x += dx
            self.current_piece.y += dy
        elif dy > 0:
            self.place_piece()

    def move_piece_abs(self, x, y, r):
        self.current_piece.rotate(r)
        self.current_piece.x = x
        self.current_piece.y = y

    def place_piece(self):
        for y, row in enumerate(self.current_piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    self.grid[self.current_piece.y + y][self.current_piece.x + x] = self.current_piece.color
        self.clear_lines()
        self.current_piece = Tetrimino(shape_index=self.pop_next_piece())
        if self.check_collision():
            self.running = False  # Game Over
        # self.solver.get_possible_placements()
        self.score += 10

    def rotate(self):
        old_tetromino = self.current_piece.tetromino
        old_rotation = self.current_piece.rotation
        self.current_piece.rotation = (self.current_piece.rotation - 1) % len(self.current_piece.shape)
        self.current_piece.tetromino = self.current_piece.shape[self.current_piece.rotation]
        if self.check_collision():
             self.current_piece.tetromino = old_tetromino
             self.current_piece.rotation = old_rotation


    def clear_lines(self):
        new_grid = [row for row in self.grid if BLACK in row]
        lines_cleared = ROWS - len(new_grid)
        self.score += (2**lines_cleared) * 100
        self.grid = [[BLACK] * COLUMNS for _ in range(lines_cleared)] + new_grid

    def update(self):
        self.move_piece(0, 1)


def point_distance(x1, y1, z1, x2, y2, z2):
    return sqrt((x1-x2)**2 + (y1-y2)**2 + (z1-z2)**2)


class Solver:

    def __init__(self, game: Tetris):
        self.descending_thread: Thread = None
        self.pos_finder_thread = None
        self.thread_active = True
        self.placment_previeing = 0
        self.game = game
        self.possible_placings_pathfound = []
        self.height_threshold = 0
        # self.all_possible_places = []
        self.past_pos = []
        self.piece_path = []
        self.best_pos = []

    def check_collision(self, dx=0, dy=0, piece=None):
        if piece is None:
            piece = self.game.current_piece
        for y, row in enumerate(piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    new_x, new_y = piece.x + x + dx, piece.y + y + dy
                    if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS or (new_y >= 0 and self.game.grid[new_y][new_x] != BLACK):
                        return True
        return False

    def sort_by_best_path(self):
        self.possible_placings_pathfound.sort(key=self.calc_pos_score, reverse=True)
        self.pos_pathfinder_thread(self.possible_placings_pathfound[0])
        self.game.current_piece.x = self.possible_placings_pathfound[0][0]
        self.game.current_piece.y = self.possible_placings_pathfound[0][1]
        self.game.current_piece.rotate(self.possible_placings_pathfound[0][2])
        self.game.place_piece()


    def calc_pos_score(self, pos, piece=None):
        if piece is None:
            piece = self.game.current_piece.copy()
        piece.rotate(pos[2])
        grid = [i.copy() for i in self.game.grid]

        for y, row in enumerate(piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    grid[pos[1] + y][pos[0] + x] = piece.color

        # score = round(20*(1-math.e**(-0.3*(pos[1]-4))))
        score = pos[1]
        for y, row in enumerate(piece.tetromino):
            for x, cell in enumerate(row):
                if cell:
                    for i in range(4):
                        new_x, new_y = pos[0] + x, pos[1] + y
                        match i:
                            case 0:
                                new_x += 1
                            case 1:
                                new_y += 1
                            case 2:
                                new_x = 1
                            case 3:
                                new_y -= 1

                        if new_y < 0:
                            continue
                        if new_x < 0 or new_x >= COLUMNS or new_y >= ROWS:
                            score += 0.6
                            continue
                        if self.game.grid[new_y][new_x] != BLACK:
                            score += 1
                            if i == 3:
                                score +=2

        self.game.overlay.fill((255, 0, 255))

        rngx = ''.join([' ' if i.count(1) == 0 else 'p' for i in map(list, zip(*piece.tetromino))])
        rngy = ''.join([' ' if i.count(1) == 0 else 'p' for i in piece.tetromino])


        hanging_blocks = 0

        for x in range(pos[0] + len(rngx.split('p')[0]) - 1, pos[0] + len(rngx.split('p')[0]) + len(rngx.strip()) + 1):
            if x < 0 or x >= COLUMNS:
                continue
            game_grid_edge_reached = False
            new_grid_edge_reached = False
            for y in range(pos[1] + len(rngy.split('p')[0]) - 1, pos[1] + len(rngy.split('p')[0]) + len(rngy.strip()) + 1):
                pygame.draw.rect(self.game.overlay, RED, (x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE), 2)
                if y < 0 or y >= ROWS:
                    continue
                if not game_grid_edge_reached and new_grid_edge_reached:
                    if grid[y][x] == BLACK:
                        score -= 5
                        # if hanging_blocks > 0:
                        #     pass
                if self.game.grid[y][x] != BLACK:
                    game_grid_edge_reached = True

                if grid[y][x] != BLACK:
                    new_grid_edge_reached = True

        return score

    def get_possible_placements(self, threaded=True):

        self.past_pos.clear()
        self.possible_placings_pathfound.clear()
        # self.all_possible_places.clear()
        self.placment_previeing = 0
        p = self.game.current_piece.copy()
        # for r in range(len(p.shape)):
        #     p.rotate(r)
        #     for x in range(-2, COLUMNS+2):
        #         for y in range(ROWS):
        #             if not self.check_collision(x, y, piece=p) and self.check_collision(x, y+1, piece=p):
        #                 self.all_possible_places.append([x, y, r])

        self.height_threshold = ROWS
        for x in range(COLUMNS):
            for y in range(ROWS):
                if y >= self.height_threshold:
                    break
                if self.game.grid[y][x] != BLACK:
                    self.height_threshold = y
                    break
        self.height_threshold -= len(p.tetromino)
        self.height_threshold = max(self.height_threshold, 0)

        if threaded:
            # self.thread_active = False
            # time.sleep(0.1)
            # self.thread_active = True
            self.descending_thread = threading.Thread(target=lambda : self.descend([p.x, self.height_threshold, p.rotation]))
            self.descending_thread.start()
        else:
            self.descend([p.x, self.height_threshold, p.rotation])

    def descend(self, pos):
        if not self.thread_active:
            return

        # time.sleep(0.03)
        p = Tetrimino(self.game.current_piece.shape_index, *pos)
        arglist = [[pos[0], pos[1], p.rotation + 1],
                   [pos[0], pos[1], p.rotation - 1],
                   [pos[0], pos[1] + 1, p.rotation],
                   [pos[0] + 1, pos[1], p.rotation],
                   [pos[0] - 1, pos[1], p.rotation]]
        # if len(p.shape) > 1:
        #     for i in range(len(p.shape)):
        #         arglist.append([pos[0], pos[1], i])
        random.shuffle(arglist)
        for c in arglist:
            p.x = c[0]
            p.y = c[1]
            p.rotate(c[2])
            i = [c[0], c[1], p.rotation]

            if self.past_pos.count(i) != 0:
                continue

            if self.check_collision(piece=p):
                continue

            self.past_pos.append(i)

            if self.check_collision(0, 1, piece=p) and self.possible_placings_pathfound.count(i) <= 0:
                self.possible_placings_pathfound.append(i)

            self.descend(i)

    def pos_pathfinder(self):
        goal = self.possible_placings_pathfound[self.placment_previeing]

        self.pos_finder_thread = threading.Thread(target=lambda: self.pos_pathfinder_thread(goal))
        self.pos_finder_thread.start()

    def ez_reachable(self, pos):
        p = Tetrimino(shape_index=self.game.current_piece.shape_index, x=pos[0], y=pos[1], rotation=pos[2])
        for i in range(pos[1]):
            if self.check_collision(piece=p, dy=-i):
                return False
        return True

    def pos_pathfinder_thread(self, start):
        p = self.game.current_piece.copy()
        goal = [p.get_x_origin(), 0, 0]

        # self.thread_active = False
        # time.sleep(0.1)
        # self.thread_active = True
        # threading.Thread(target=lambda : descend([p.x, p.y, p.rotation])).start()

        queue = []

        destination_not_reached = True

        pos = start

        pos_pool = [i for i in self.past_pos]
        # pos_pool = [i if i[1] >= goal[1] for i in self.past_pos]


        def append(target: list, a: list):
            for i in range(len(target)):
                if a[0] == target[i][0]:
                    if a[3] < target[i][3]:
                        target.pop(i)
                        target.insert(i, a)
                    return
            target.insert(0, a)


        queue.append([pos, [], point_distance(*pos, *goal), 0])

        if pos == goal:
            return

        while destination_not_reached:

            l = min(queue, key=lambda x: x[2] + x[3] + (x[2] / 20))
            pos, log, h, f = l
            queue.remove(l)

            self.piece_path = []
            # for i in queue:
            #     self.piece_path.extend(i[1])

            if pos == goal:
                self.piece_path = log + [pos]
                break

            if self.ez_reachable(pos):
                self.piece_path = self.autocomplete_path_to_origin(log + [pos])
                break

            t = [pos[0]+1, pos[1], pos[2]]
            if t in pos_pool and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f + 1])

            t = [pos[0]-1, pos[1], pos[2]]
            if t in pos_pool and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f + 1])

            t = [pos[0], pos[1]-1, pos[2]]
            if t in pos_pool and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f + 1])

            t = [pos[0], pos[1], p.modulo_rotation(pos[2] + 1)]
            if t in pos_pool and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f + 1])

            t = [pos[0], pos[1], p.modulo_rotation(pos[2] - 1)]
            if t in pos_pool and t not in log:
                append(queue, [t, log + [pos], point_distance(*t, *goal), f + 1])
        self.piece_path.reverse()


    def autocomplete_path_to_origin(self, path: list):
        if len(path) == 0:
            return
        last_pos = path[-1]
        for i in range(last_pos[1] - self.game.current_piece.y):
            path.append([last_pos[0], last_pos[1] - 1 - i, last_pos[2]])
        last_pos = path[-1]
        for i in range(abs(self.game.current_piece.x - last_pos[0])):
            if last_pos[0] < self.game.current_piece.x:
                path.append([last_pos[0] + 1 + i, last_pos[1], last_pos[2]])
            else:
                path.append([last_pos[0] - 1 - i, last_pos[1], last_pos[2]])
        last_pos = path[-1]
        match last_pos[2]:
            case 1:
                path.append([last_pos[0], last_pos[1], 0])
            case 2:
                path.append([last_pos[0], last_pos[1], 1])
                path.append([last_pos[0], last_pos[1], 0])
            case 3:
                path.append([last_pos[0], last_pos[1], 0])
        return path

    def autosolve(self):
        while True:
            # time.sleep(1)
            # self.descending_thread.join()
            self.get_possible_placements(threaded=False)
            if len(self.possible_placings_pathfound) == 0:
                self.game.place_piece()
                return
            best_pos = self.possible_placings_pathfound[0]
            best_score = 0
            scores = []
            for n, i in enumerate(self.possible_placings_pathfound):
                score = self.calc_pos_score(i)
                scores.append([score, i])
                if score > best_score:
                    best_score = score
                    best_pos = i
                    self.placment_previeing = n

            # self.calc_pos_score(best_pos)
            #
            # self.pos_pathfinder_thread(best_pos)
            # for pos in self.piece_path:
            #     time.sleep(0.2)
            #     self.game.move_piece_abs(*pos)
            self.game.move_piece_abs(*best_pos)
            self.game.place_piece()

# Game Loop
def main():
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    overlay = pygame.surface.Surface((WIDTH, HEIGHT))
    overlay.set_colorkey((255,0,255))
    overlay.fill((255,0,255))
    clock = pygame.time.Clock()
    game = Tetris(overlay)

    Thread(target=game.solver.autosolve).start()
    while game.running:
        screen.fill(BLACK)
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                game.running = False
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_TAB:
                    game.paused = not game.paused
                    game.key_pressed = True
                if event.key == pygame.K_LEFT:
                    game.move_piece(-1, 0)
                elif event.key == pygame.K_RIGHT:
                    game.move_piece(1, 0)
                elif event.key == pygame.K_DOWN:
                    game.move_piece(0, 1)
                elif event.key == pygame.K_UP:
                    game.move_piece(0, -1)
                elif event.key == pygame.K_x:
                    game.rotate()
                elif event.key == pygame.K_SPACE:
                    piece = game.current_piece
                    while game.current_piece == piece and game:
                        game.move_piece(0, 1)
                elif event.key == pygame.K_p:
                    game.paused=True
                    game.solver.get_possible_placements()
                elif event.key == pygame.K_MINUS:
                    game.solver.placment_previeing -= 1
                elif event.key == pygame.K_EQUALS:
                    game.solver.placment_previeing += 1
                elif event.key == pygame.K_o:
                    game.solver.pos_pathfinder()
                elif event.key == pygame.K_i:
                    game.solver.sort_by_best_path()
                elif event.key == pygame.K_k:
                    Thread(target=game.solver.autosolve).start()


        if game.last_update + 1 < time.time():

            game.last_update = time.time()

            if not game.paused:
                game.update()

        game.draw_grid(screen)
        game.draw_placment_preview(screen)
        game.draw_piece(screen)
        game.draw_paths(screen)

        # Display score
        if game.paused:
            pygame.draw.rect(screen, WHITE, ((10, 10), (20, 20)))
        else:
            pygame.draw.polygon(screen, WHITE, ((10, 10), (30, 20), (10, 30)))

        # to_render = f"Score: {game.score}  {game.solver.placment_previeing}/{len(game.solver.possible_placings_pathfound)}"
        # score_text = font.render(to_render, True, WHITE)
        # screen.blit(score_text, (50, 10))

        pygame.display.flip()


    pygame.quit()

    score_list.append(game.score)
    print(f"[{time.time()}]score: {game.score}")

if __name__ == "__main__":
    # main()


    print('\n')
    score_list = []
    for i in range(10):
        main()
    print(f"max: {max(score_list)}, min: {min(score_list)}, avrg: {sum(score_list) / len(score_list)}, median: {median(score_list)}")
